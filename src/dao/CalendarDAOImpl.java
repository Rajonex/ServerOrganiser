package dao;

import java.sql.Date;
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

import connection.ConnectionProvider;
import sendutils.GroupCalendar;
import utils.Day;

public class CalendarDAOImpl implements CalendarDAO{
	
	private static final String READ_GROUP_CALENDARS = "SELECT * FROM calendar WHERE group_id=:group_id;";
	private static final String READ_ALL_BY_TOKEN = "SELECT * FROM calendar WHERE teacher_token=:teacher_token;";
	private static final String READ_DAY_CALENDAR = "SELECT * FROM calendar WHERE teacher_token=:teacher_token AND day=:day;";
	private static final String CREATE_GROUPCALENDAR = "INSERT INTO calendar(day, lesson_time, group_id, teacher_token) SELECT :day, :lesson_time, :group_id, :teacher_token WHERE EXISTS(SELECT * FROM teacher WHERE teacher.token=:teacher_token);";
	private static final String DELETE_GROUP_CALENDAR = "DELETE FROM calendar WHERE group_id=:group_id;";
	
	private NamedParameterJdbcTemplate template;
	
	CalendarDAOImpl()
	{
		template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
	}
	
	public boolean addGroupCalendar(GroupCalendar calendar, long groupId)
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
	
	public boolean deleteGroupCalendar(long groupId)
	{
		boolean result = false;
		SqlParameterSource paramSource = new MapSqlParameterSource("group_id", groupId);
		int update = template.update(DELETE_GROUP_CALENDAR, paramSource);
		if (update > 0) {
			result = true;
		}
		return result;
	}

	@Override
	public boolean create(GroupCalendar newObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GroupCalendar read(Long primaryKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(GroupCalendar updateObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Long key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<GroupCalendar> getCalendarByToken(String token) {
		SqlParameterSource paramSource = new MapSqlParameterSource("teacher_token", token);
		List<GroupCalendar> listCalendar = template.query(READ_ALL_BY_TOKEN, paramSource, new CalendarRowMapper());
		return listCalendar;
	}

	@Override
	public List<GroupCalendar> getCalendarByDate(Day day, String token) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("teacher_token", token);
		paramMap.put("day", day.toString());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		List<GroupCalendar> listCalendar = template.query(READ_DAY_CALENDAR, paramSource, new CalendarRowMapper());
		return listCalendar;
	}

	@Override
	public List<GroupCalendar> getGroupsCalendars(long groupId) {
		SqlParameterSource paramSource = new MapSqlParameterSource("group_id", groupId);
		List<GroupCalendar> listCalendar = template.query(READ_GROUP_CALENDARS, paramSource, new CalendarRowMapper());
		return listCalendar;
	}

	@Override
	public List<GroupCalendar> getGroupsCalendarByDate(long groupId, Date date) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class CalendarRowMapper implements RowMapper<GroupCalendar>{

		@Override
		public GroupCalendar mapRow(ResultSet arg0, int arg1) throws SQLException {
			GroupCalendar calendarGroup = new GroupCalendar();
			calendarGroup.setDay(Day.valueOf(arg0.getString("day")));
//			calendarGroup.setDay(arg0.getString("day"));
			calendarGroup.setGroupId(arg0.getLong("group_id"));
			calendarGroup.setId(arg0.getInt("calendar_id"));
			calendarGroup.setTeacherToken(arg0.getString("teacher_token"));
			calendarGroup.setTime(arg0.getTime("lesson_time").getTime());
			return calendarGroup;
		}
		
	}

	@Override
	public List<GroupCalendar> getAll() {
		// TODO Auto-generated method stub
		return null;
	}



}
