package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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
import sends.Group;
import sends.MiniGroup;
import sends.Student;
import sendutils.GroupCalendar;

public class GroupDAOImpl implements GroupDAO {

	private static final String CREATE_GROUP = "INSERT INTO model(group_name, rate, teacher_token) SELECT :group_name, :rate, :teacher_token WHERE EXISTS(SELECT * FROM teacher WHERE token=:token);";
	private static final String CREATE_STUDENTGROUP = "INSERT INTO student_group(student_id, group_id) SELECT :student_id, :group_id WHERE EXISTS(SELECT * FROM student WHERE student_id=:student_id);";
	private static final String CREATE_GROUPCALENDAR = "INSERT INTO calendar(day, lesson_time, group_id, teacher_token) SELECT :day, :lesson_time, :group_id, :teacher_token WHERE EXISTS(SELECT * FROM teacher WHERE teacher.token=:teacher_token);";
	private static final String UPDATE_GROUP = "UPDATE model SET group_name=:group_name, rate=:rate WHERE teacher_token=:teacher_token AND group_id=:group_id;";
	private static final String READ_GROUP = "SELECT group_id, group_name, rate, group_activity, teacher_token FROM model WHERE group_id = :group_id;";
	private static final String READ_GROUP_WITH_TOKEN = "SELECT group_id, group_name, rate, group_activity, teacher_token FROM model WHERE group_id = :group_id AND teacher_token=:teacher_token;";
	private static final String READ_MINI_GROUPS = "SELECT group_id, group_name FROM model WHERE teacher_token=:teacher_token;";
	private static final String DELETE_STUDENTGROUP = "DELETE FROM student_group WHERE group_id=:group_id;";
	
	private NamedParameterJdbcTemplate template;

	public GroupDAOImpl() {
		template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
	}

	@Override
	public boolean create(Group newObject) {
		boolean result = false;
		KeyHolder holder = new GeneratedKeyHolder();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("group_name", newObject.getName());
		paramMap.put("rate", newObject.getRate());
		paramMap.put("teacher_token", newObject.getTeacherToken());
		paramMap.put("token", newObject.getTeacherToken());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(CREATE_GROUP, paramSource, holder);
		if (update > 0) {
			Long genGroupId = (Long) holder.getKey();
			boolean success = true;
			List<Student> studentList = newObject.getStudents();
			if (studentList != null) {
				for (Student student : studentList) {
					if(!addStudentGroup(student.getId(), genGroupId))
					{
						success = false;
					}
				}
			}
			
			List<GroupCalendar> calendarList = newObject.getGroupCalendar();
			if(calendarList != null)
			{
				for(GroupCalendar groupCalendar : calendarList)
				{
					if(!addGroupCalendar(groupCalendar, genGroupId))
					{
						success = false;
					}
				}
			}
			
			result = success;
		}
		return result;
	}

	public boolean addStudentGroup(long studentId, long groupId) {
		boolean result = false;
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("student_id", studentId);
		paramMap.put("group_id", groupId);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(CREATE_STUDENTGROUP, paramSource);
		if (update > 0) {
			result = true;
		}
		return result;
	}
	
	public boolean deleteStudentGroup(long groupId)
	{
		boolean result = false;
		SqlParameterSource paramSource = new MapSqlParameterSource("group_id", groupId);
		int update = template.update(DELETE_STUDENTGROUP, paramSource);
		if (update > 0) {
			result = true;
		}
		return result;
	}
	
	
	private boolean addGroupCalendar(GroupCalendar calendar, long groupId)
	{
		boolean result = false;
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("day", calendar.getDay().toString());
		Time time = new Time(calendar.getTime());
		paramMap.put("lesson_time", time);
		paramMap.put("group_id", groupId);
		paramMap.put("teacher_token", calendar.getTeacherToken());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(CREATE_GROUPCALENDAR, paramSource);
		if (update > 0) {
			result = true;
		}
		return result;
	}

	@Override
	public Group read(Long primaryKey) {
		SqlParameterSource paramSource = new MapSqlParameterSource("group_id", primaryKey);
		List<Group> listGroup = template.query(READ_GROUP, paramSource, new PartGroupRowMapper());
		Group resultGroup = (listGroup.size() > 0) ? listGroup.get(0) : null;
		return resultGroup;
		
	}
	
	public Group getGroupByIdAndToken(long id, String token)
	{
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("group_id", id);
		paramMap.put("teacher_token", token);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		List<Group> listGroup = template.query(READ_GROUP_WITH_TOKEN, paramSource, new PartGroupRowMapper());
		Group resultGroup = (listGroup.size() > 0) ? listGroup.get(0) : null;
		return resultGroup;
	}


	@Override
	public boolean update(Group updateObject) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("group_name", updateObject.getName());
		paramMap.put("rate", updateObject.getRate());
		paramMap.put("teacher_token", updateObject.getTeacherToken());
		paramMap.put("group_id", updateObject.getId());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(UPDATE_GROUP, paramSource);
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
	public List<Group> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MiniGroup> getTeachersMiniGroups(String token) {
		SqlParameterSource paramSource = new MapSqlParameterSource("teacher_token", token);
		List<MiniGroup> listGroups = template.query(READ_MINI_GROUPS, paramSource, new MiniGroupRowMapper());
		return listGroups;
	}
	
	private class MiniGroupRowMapper implements RowMapper<MiniGroup>
	{

		@Override
		public MiniGroup mapRow(ResultSet arg0, int arg1) throws SQLException {
			MiniGroup resultGroup = new MiniGroup();
			resultGroup.setId(arg0.getLong("group_id"));
			resultGroup.setName(arg0.getString("group_name"));
			return resultGroup;
		}
		
	}
	
	private class PartGroupRowMapper implements RowMapper<Group>
	{

		@Override
		public Group mapRow(ResultSet arg0, int arg1) throws SQLException {
			Group resultGroup = new Group();
			resultGroup.setActivity(arg0.getBoolean("group_activity"));
			resultGroup.setId(arg0.getLong("group_id"));
			resultGroup.setName(arg0.getString("group_name"));
			resultGroup.setRate(arg0.getDouble("rate"));
			resultGroup.setTeacherToken(arg0.getString("teacher_token"));
			return resultGroup;
		}
		
	}

}
