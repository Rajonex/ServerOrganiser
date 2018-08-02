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

import connection.ConnectionProvider;
import sends.Teacher;

public class TeacherDAOImpl implements TeacherDAO {

	private static final String CREATE_TEACHER = "INSERT INTO teacher(teacher_name, password, token, login) VALUES(:teacher_name, :password, :token, :login);";

//	private static final String READ_TEACHER = "SELECT teacher_id, teacher_name, password, token FROM teacher WHERE teacher_id = :teacher_id";

	private static final String READ_TEACHER_BY_TOKEN = "SELECT teacher_name, password, token, login FROM teacher WHERE token = :token";

	// private static final String READ_TEACHER_BY_NAME = "SELECT teacher_id,
	// teacher_name, password, token FROM teacher WHERE teacher_name =
	// :teacher_name";

	private static final String IS_EXISTS_LOGIN = "SELECT COUNT(1) FROM teacher WHERE login=:login";

	private static final String IS_EXISTS_TOKEN = "SELECT COUNT(1) FROM teacher WHERE token=:token";

	private static final String READ_TEACHER_LOGIN = "SELECT teacher_name, password, token, login FROM teacher WHERE login=:login AND password=:password";

	private NamedParameterJdbcTemplate template;

	public TeacherDAOImpl() {
		template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
	}

	@Override
	public boolean create(Teacher newObject) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("teacher_name", newObject.getName());
		paramMap.put("password", newObject.getPassword());
		paramMap.put("token", newObject.getToken());
		paramMap.put("login", newObject.getLogin());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(CREATE_TEACHER, paramSource);
		if (update > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public Teacher read(Long primaryKey) {
//		Teacher resultTeacher = null;
//		SqlParameterSource paramSource = new MapSqlParameterSource("teacher_id", primaryKey);
//		resultTeacher = template.queryForObject(READ_TEACHER, paramSource, new TeacherRowMapper());
//		return resultTeacher;
		return null;
	}

	@Override
	public boolean update(Teacher updateObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Long key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Teacher> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Teacher readByNameAndPassword(String name, String password) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("login", name);
		paramMap.put("password", password);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		List<Teacher> listTeachers = template.query(READ_TEACHER_LOGIN, paramSource, new TeacherRowMapper());
		// Teacher resTeacher = template.queryForObject(READ_TEACHER_LOGIN, paramSource,
		// new TeacherRowMapper());
		Teacher resTeacher = (listTeachers.size() > 0) ? listTeachers.get(0) : null;
		return resTeacher;
	}

	@Override
	public Teacher readByToken(String token) {
		List<Teacher> listTeachers;
		SqlParameterSource paramSource = new MapSqlParameterSource("token", token);
		listTeachers = template.query(READ_TEACHER_BY_TOKEN, paramSource, new TeacherRowMapper());
		Teacher resultTeacher = (listTeachers.size() > 0) ? listTeachers.get(0) : null;
		return resultTeacher;
	}
	//
	// @Override
	// public Teacher readByName(String name) {
	// Teacher resultTeacher = null;
	// SqlParameterSource paramSource = new MapSqlParameterSource("teacher_name",
	// name);
	// resultTeacher = template.queryForObject(READ_TEACHER_BY_NAME, paramSource,
	// new TeacherRowMapper());
	// return resultTeacher;
	// }

	@Override
	public boolean isExistsTeacherWithLogin(String login) {
		SqlParameterSource paramSource = new MapSqlParameterSource("login", login);
		boolean result = (template.queryForObject(IS_EXISTS_LOGIN, paramSource, new ExistsRowMapper()) > 0) ? true
				: false;
		return result;
	}

	@Override
	public boolean isExistsTeacherWithToken(String token) {
		SqlParameterSource paramSource = new MapSqlParameterSource("token", token);

		boolean result = (template.queryForObject(IS_EXISTS_TOKEN, paramSource, new ExistsRowMapper()) > 0) ? true
				: false;
		return result;
	}

	private class TeacherRowMapper implements RowMapper<Teacher> {

		@Override
		public Teacher mapRow(ResultSet arg0, int arg1) throws SQLException {
			Teacher teacher = new Teacher();

			teacher.setName(arg0.getString("teacher_name"));
			teacher.setPassword(arg0.getString("password"));
			teacher.setToken(arg0.getString("token"));
			teacher.setLogin(arg0.getString("login"));

			return teacher;
		}

	}

	private class ExistsRowMapper implements RowMapper<Integer> {

		@Override
		public Integer mapRow(ResultSet arg0, int arg1) throws SQLException {

			return arg0.getInt("COUNT(1)");
		}

	}

}
