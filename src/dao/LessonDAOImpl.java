package dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import connection.ConnectionProvider;
import sends.Lesson;
import sends.MiniLesson;
import sends.Student;
import sendutils.StudentPresent;

public class LessonDAOImpl implements LessonDAO {
	
	private static final String CREATE_LESSON = "INSERT INTO lesson(topic, description, lesson_date, group_id, teacher_token) SELECT :topic, :description, :lesson_date, :group_id, :teacher_token WHERE EXISTS(SELECT * FROM teacher WHERE token=:teacher_token) AND EXISTS(SELECT * FROM model WHERE group_id=:group_id);";
	private static final String CREATE_PRESENT = "INSERT INTO present(lesson_id, student_id, is_present) SELECT :lesson_id, :student_id, :is_present WHERE EXISTS(SELECT * FROM student WHERE student_id=:student_id);";
	private static final String UPDATE_LESSON = "UPDATE lesson SET topic=:topic, description=:description, lesson_date=:lesson_date WHERE teacher_token=:teacher_token AND lesson_id=:lesson_id;";
	private static final String UPDATE_PRESENT = "UPDATE present SET is_present=:is_present WHERE present_id=:present_id AND lesson_id=:lesson_id;";
	private static final String READ_LESSON = "SELECT * FROM lesson WHERE lesson_id=:lesson_id;";
	private static final String READ_LESSON_WITH_TOKEN = "SELECT * FROM lesson WHERE lesson_id=:lesson_id AND teacher_token=:teacher_token;";
	private static final String READ_MINI_LESSONS = "SELECT lesson_id, topic FROM lesson WHERE group_id=:group_id AND teacher_token=:teacher_token;";
	private static final String ADD_SALDO_FOR_STUDENT = "INSERT INTO saldo(student_id, group_id, lessons_number, to_pay, paid, saldo_date)  VALUES (:student_id, :group_id, :presence, :cost, 0, :saldo_date);";
	private static final String UPDATE_SALDO_FOR_STUDENT = "UPDATE saldo SET lessons_number=lessons_number+:presence, to_pay=to_pay+:cost WHERE month(saldo_date)=:month_date AND year(saldo_date)=:year_date AND student_id=:student_id AND group_id=:group_id";
	private static final String READ_PRESENCE = "SELECT student.student_id, firstname, lastname, phone, email, student_activity, teacher_token, present_id, is_present FROM student INNER JOIN present ON student.student_id = present.student_id WHERE present_id=:present_id;";


	
	/*
	 * "IF EXISTS (SELECT * FROM saldo WHERE month(saldo_date)=:month_date AND year(saldo_date)=:year_date AND student_id=:student_id AND group_id=:group_id) "
	 * +
	 * "UPDATE saldo SET lesson_number=lesson_number+:presence, to_pay=to_pay+:cost WHERE month(saldo_date)=:month_date AND year(saldo_date)=:year_date AND student_id=:student_id AND group_id=:group_id "
	 * +
	 * "ELSE INSERT INTO saldo(student_id, group_id, lesson_number, to_pay, paid, saldo_date)  VALUES (:student_id, :group_id, :presence, :cost, 0, :saldo_date);"
	 * ;
	 */
	
	private NamedParameterJdbcTemplate template;
	
	public LessonDAOImpl()
	{
		template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
	}
	
	
	public int safeCreate(Lesson lesson, double lessonCost)
	{
		int result = 1;
		KeyHolder holder = new GeneratedKeyHolder();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("topic", lesson.getTopic());
		paramMap.put("description", lesson.getDescription());
		paramMap.put("lesson_date", new Date(lesson.getDate()));
		paramMap.put("group_id", lesson.getGroupId());
		paramMap.put("teacher_token", lesson.getTeacherToken());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(CREATE_LESSON, paramSource, holder);
		if (update > 0)
		{
			result = 0;
			long lessonId = (Long) holder.getKey();
			long groupId = lesson.getGroupId();
			long date = lesson.getDate();
			for (StudentPresent sp : lesson.getStudentPresent())
			{
				if (addPresent(lessonId, sp.getStudent().getId(), sp.isPresence()))
				{
					if (!addPayment(sp.getStudent().getId(), groupId, date, sp.isPresence(), lessonCost))
					{
						result = 3;
					}
				} else
				{
					if (result != 3)
					{
						result = 2;
					}
				}
			}

		}
		return result;
	}
	
	private boolean addPayment(long studentId, long groupId, long date, boolean presence, double cost)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date saldoDate = new Date(calendar.getTimeInMillis());
		int presenceLesson = presence ? 1 : 0;
		double lessonCost = presence ? cost : 0.0;

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("saldo_date", saldoDate);
		paramMap.put("month_date", calendar.get(Calendar.MONTH)+1);
		paramMap.put("year_date", calendar.get(Calendar.YEAR));
		paramMap.put("student_id", studentId);
		paramMap.put("group_id", groupId);
		paramMap.put("presence", presenceLesson);
		paramMap.put("cost", lessonCost);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(UPDATE_SALDO_FOR_STUDENT, paramSource);
		if (update > 0)
		{
			return true;
		}
		else
		{
			int updateTwo = template.update(ADD_SALDO_FOR_STUDENT, paramSource);
			if (updateTwo > 0)
			{
				return true;
			}
		}
		return false;

	}
	
	private boolean addPresent(long lessonId, long studentId, boolean isPresent)
	{
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("lesson_id", lessonId);
		paramMap.put("student_id", studentId);
		paramMap.put("is_present", isPresent);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(CREATE_PRESENT, paramSource);
		if(update > 0)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean create(Lesson newObject)  {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Lesson read(Long primaryKey) {
		SqlParameterSource paramSource = new MapSqlParameterSource("lesson_id", primaryKey);
		List<Lesson> lessons = template.query(READ_LESSON, paramSource, new LessonRowMapper());
		Lesson lesson = (lessons.size() > 0) ? lessons.get(0) : null;
		return lesson;
	}
	
	public Lesson getLessonByTokenAndId(long id, String token)
	{
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("lesson_id", id);
		paramMap.put("teacher_token", token);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		List<Lesson> lessons = template.query(READ_LESSON_WITH_TOKEN, paramSource, new LessonRowMapper());
		Lesson lesson = (lessons.size() > 0) ? lessons.get(0) : null;
		return lesson;
	}
	

	@Override
	public boolean updateAll(Lesson updateObject, double rate) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("topic", updateObject.getTopic());
		paramMap.put("description", updateObject.getDescription());
		paramMap.put("lesson_date", new Date(updateObject.getDate()));
		paramMap.put("teacher_token", updateObject.getTeacherToken());
		paramMap.put("lesson_id", updateObject.getId());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(UPDATE_LESSON, paramSource);
		if(update > 0)
		{
			long lessonId = updateObject.getId();
			flag = true;
			for(StudentPresent present: updateObject.getStudentPresent())
			{
				if(!updatePresent(lessonId, present, rate, updateObject.getGroupId(), updateObject.getDate()))
				{
					flag = false;
				}
			}
		}
			
		return flag;
	}


	private boolean updatePresent(long lessonId, StudentPresent present, double rate, long groupId, long date)
	{
		boolean flag = false;
		StudentPresent previousState = getLastPresence(present.getId());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("is_present", present.isPresence());
		paramMap.put("present_id", present.getId());
		paramMap.put("lesson_id", lessonId);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(UPDATE_PRESENT, paramSource);
		if(update > 0)
		{
			flag = true;
			if(previousState != null && previousState.isPresence() == false && present.isPresence() == true)
			{
				// increase value
				if(!updatePayment(present.getStudent().getId(), groupId, date, 1, rate))
					flag = false;
			}
			else
			{
				if(previousState != null && previousState.isPresence() == true && present.isPresence() == false)
				{
					// decrease value
					if(!updatePayment(present.getStudent().getId(), groupId, date, -1, rate*(-1.0)))
						flag = false;
				}
			}

		}
			
		return flag;
	}
	
	private boolean updatePayment(long studentId, long groupId, long date, int presence, double cost)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("month_date", calendar.get(Calendar.MONTH)+1);
		paramMap.put("year_date", calendar.get(Calendar.YEAR));
		paramMap.put("student_id", studentId);
		paramMap.put("group_id", groupId);
		paramMap.put("presence", presence);
		paramMap.put("cost", cost);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(UPDATE_SALDO_FOR_STUDENT, paramSource);
		if(update > 0)
			return true;
		
		return false;
	}
	
	private StudentPresent getLastPresence(long presenceId)
	{
		SqlParameterSource paramSource = new MapSqlParameterSource("present_id", presenceId);
		List<StudentPresent> presences = template.query(READ_PRESENCE, paramSource, new PresenceRowMapper());
		StudentPresent studentPresent = (presences.size() > 0) ? presences.get(0) : null;
		return studentPresent;
	}

	@Override
	public boolean delete(Long key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Lesson> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MiniLesson> getGroupsMiniLessons(long groupId, String token) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("group_id", groupId);
		paramMap.put("teacher_token", token);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		List<MiniLesson> miniLessons = template.query(READ_MINI_LESSONS, paramSource, new MiniLessonRowMapper());
		return miniLessons;
	}
	
	
	
	private class LessonRowMapper implements RowMapper<Lesson>
	{

		@Override
		public Lesson mapRow(ResultSet arg0, int arg1) throws SQLException {
			Lesson lesson = new Lesson();
			lesson.setDate(arg0.getDate("lesson_date").getTime());
			lesson.setDescription(arg0.getString("description"));
			lesson.setGroupId(arg0.getLong("group_id"));
			lesson.setId(arg0.getLong("lesson_id"));
			lesson.setTeacherToken(arg0.getString("teacher_token"));
			lesson.setTopic(arg0.getString("topic"));
			return lesson;
		}
		
	}
	
	private class MiniLessonRowMapper implements RowMapper<MiniLesson>
	{

		@Override
		public MiniLesson mapRow(ResultSet arg0, int arg1) throws SQLException {
			MiniLesson miniLesson = new MiniLesson();
			miniLesson.setId(arg0.getLong("lesson_id"));
			miniLesson.setTopic(arg0.getString("topic"));
			return miniLesson;
		}
		
	}
	
	private class PresenceRowMapper implements RowMapper<StudentPresent>{

		@Override
		public StudentPresent mapRow(ResultSet arg0, int arg1) throws SQLException {
			Student student = new Student();
			student.setActivity(arg0.getBoolean("student_activity"));
			student.setEmail(arg0.getString("email"));
			student.setFirstname(arg0.getString("firstname"));
			student.setId(arg0.getLong("student_id"));
			student.setLastname(arg0.getString("lastname"));
			student.setPhone(arg0.getString("phone"));
			student.setTeacherToken(arg0.getString("teacher_token"));
			StudentPresent studentPresent = new StudentPresent();
			studentPresent.setId(arg0.getLong("present_id"));
			studentPresent.setPresence(arg0.getBoolean("is_present"));
			studentPresent.setStudent(student);
			return studentPresent;
		}
		
	}

	@Override
	public boolean update(Lesson updateObject)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
