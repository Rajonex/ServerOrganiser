package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import sends.Student;
import sendutils.StudentPresent;

public class StudentDAOImpl implements StudentDAO {

	private static final String CREATE_STUDENT = "INSERT INTO student(firstname, lastname, phone, email, teacher_token) SELECT :firstname, :lastname, :phone, :email, :teacher_token WHERE EXISTS (SELECT * FROM teacher WHERE token=:teacher_token);";
	private static final String UPDATE_STUDENT = "UPDATE student SET firstname=:firstname, lastname=:lastname, phone=:phone, email=:email WHERE student_id=:student_id AND teacher_token=:teacher_token;";
	private static final String UPDATE_STUDENT_ACTIVITY = "UPDATE student SET student_activity=:student_activity WHERE student_id=:student_id AND teacher_token=:teacher_token;";
	private static final String READ_ALL_STUDENTS = "SELECT * FROM student WHERE teacher_token=:teacher_token AND student_activity=true";
	private static final String READ_GROUP_STUDENTS = "SELECT * FROM student INNER JOIN student_group ON student.student_id = student_group.student_id WHERE group_id = :group_id AND teacher_token=:teacher_token";
	private static final String READ_PRESENCE = "SELECT student.student_id, firstname, lastname, phone, email, student_activity, teacher_token, present_id, is_present FROM student INNER JOIN present ON student.student_id = present.student_id WHERE lesson_id=:lesson_id;";
	
	
	private NamedParameterJdbcTemplate template;
	
	public StudentDAOImpl()
	{
		template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
		
	}
	
	@Override
	public boolean create(Student newObject) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("firstname", newObject.getFirstname());
		paramMap.put("lastname", newObject.getLastname());
		paramMap.put("phone", newObject.getPhone());
		paramMap.put("email", newObject.getEmail());
		paramMap.put("teacher_token", newObject.getTeacherToken());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
//		KeyHolder holder = new GeneratedKeyHolder();
		int update = template.update(CREATE_STUDENT, paramSource);
		if(update > 0)
			flag = true;
		return flag;
	}

	@Override
	public Student read(Long primaryKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Student updateObject) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("firstname", updateObject.getFirstname());
		paramMap.put("lastname", updateObject.getLastname());
		paramMap.put("phone", updateObject.getPhone());
		paramMap.put("email", updateObject.getEmail());
		paramMap.put("student_id", updateObject.getId());
		paramMap.put("teacher_token", updateObject.getTeacherToken());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(UPDATE_STUDENT, paramSource);
		if(update > 0)
			flag = true;
		return flag;
	}
	
	@Override
	public boolean updateActivity(Student updateObject) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("student_activity", updateObject.isActivity());
		paramMap.put("student_id", updateObject.getId());
		paramMap.put("teacher_token", updateObject.getTeacherToken());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(UPDATE_STUDENT_ACTIVITY, paramSource);
		if(update > 0)
			flag = true;
		return flag;
	}
	
	

	@Override
	public boolean delete(Long key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Student> getAllForTeacher(String token) {
		SqlParameterSource paramSource = new MapSqlParameterSource("teacher_token", token);
		List<Student> students = template.query(READ_ALL_STUDENTS, paramSource, new StudentRowMapper());
		return students;
	}

	@Override
	public List<Student> getByGroup(long groupId, String token) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("group_id", groupId);
		paramMap.put("teacher_token", token);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		List<Student> resultList = template.query(READ_GROUP_STUDENTS, paramSource, new StudentRowMapper());
		return resultList;
	}
	
	public List<StudentPresent> getPresenceForLesson(long lessonId)
	{
		SqlParameterSource paramSource = new MapSqlParameterSource("lesson_id", lessonId);
		List<StudentPresent> presences = template.query(READ_PRESENCE, paramSource, new PresenceRowMapper());
		return presences;		
	}
	
	private class StudentRowMapper implements RowMapper<Student>{

		@Override
		public Student mapRow(ResultSet arg0, int arg1) throws SQLException {
			Student student = new Student();
			student.setActivity(arg0.getBoolean("student_activity"));
			student.setEmail(arg0.getString("email"));
			student.setFirstname(arg0.getString("firstname"));
			student.setId(arg0.getLong("student_id"));
			student.setLastname(arg0.getString("lastname"));
			student.setPhone(arg0.getString("phone"));
			student.setTeacherToken(arg0.getString("teacher_token"));
			return student;
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
	public List<Student> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
