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
import sends.Note;

public class NoteDAOImpl implements NoteDAO{
	
	private static final String CREATE_NOTE = "INSERT INTO note(teacher_token, note_text, note_title) VALUES(:teacher_token, :note_text, :note_title);";
	private static final String CREATE_NOTE_WITH_CONFIRM = "INSERT INTO note(teacher_token, note_text, note_title) SELECT :teacher_token, :note_text, :note_title WHERE EXISTS(SELECT * FROM teacher WHERE token=:token);";
	private static final String READ_TEACHERS_NOTES = "SELECT note_id, note_text, note_title, teacher_token FROM note WHERE teacher_token=:teacher_token";
	private static final String DELETE_TEACHER_NOTE = "DELETE FROM note WHERE note_id=:note_id AND teacher_token=:teacher_token;";
	
	private NamedParameterJdbcTemplate template;
	
	public NoteDAOImpl()
	{
		template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
	}

	@Override
	public boolean create(Note newObject) {
//		Note resultNote = new Note(newObject);
//		KeyHolder holder = new GeneratedKeyHolder();
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("teacher_token", newObject.getTeacherToken());
//		paramMap.put("note_text", newObject.getText());
//		paramMap.put("note_title", newObject.getTitle());
//		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
//		int update = template.update(CREATE_NOTE, paramSource, holder);
//		if(update > 0)
//			resultNote.setId((Long)holder.getKey());
//		return resultNote;
		
		boolean flag = false;
		KeyHolder holder = new GeneratedKeyHolder();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("token", newObject.getTeacherToken());
		paramMap.put("teacher_token", newObject.getTeacherToken());
		paramMap.put("note_text", newObject.getText());
		paramMap.put("note_title", newObject.getTitle());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(CREATE_NOTE_WITH_CONFIRM, paramSource, holder);
		if(update > 0)
			flag = true;
		return flag;
	}
	
//	@Override
//	public boolean safeCreateWithConfirm(Note note) {
//		boolean flag = false;
//		KeyHolder holder = new GeneratedKeyHolder();
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("token", note.getTeacherToken());
//		paramMap.put("teacher_token", note.getTeacherToken());
//		paramMap.put("note_text", note.getText());
//		paramMap.put("note_title", note.getTitle());
//		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
//		int update = template.update(CREATE_NOTE_WITH_CONFIRM, paramSource, holder);
//		if(update > 0)
//			flag = true;
//		return flag;
//	}

	@Override
	public Note read(Long primaryKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Note updateObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Long key) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean deleteTeachersNote(Long key, String teacherToken)
	{
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("teacher_token", teacherToken);
		paramMap.put("note_id", key);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(DELETE_TEACHER_NOTE, paramSource);
		if(update > 0)
			flag = true;
		return flag;
	}

	@Override
	public List<Note> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Note> getTeachersNotes(String teacherToken) {
		SqlParameterSource paramSource = new MapSqlParameterSource("teacher_token", teacherToken);
		List<Note> resultList = template.query(READ_TEACHERS_NOTES, paramSource, new NoteRowMapper());
		return resultList;
	}

	private class NoteRowMapper implements RowMapper<Note>{

		@Override
		public Note mapRow(ResultSet arg0, int arg1) throws SQLException {
			Note note = new Note();
			note.setId(arg0.getLong("note_id"));
			note.setTeacherToken(arg0.getString("teacher_token"));
			note.setText(arg0.getString("note_text"));
			note.setTitle(arg0.getString("note_title"));
			return note;
		}
		
	}
}
