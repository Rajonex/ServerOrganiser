package dao;

import java.util.List;

import sends.Note;

public interface NoteDAO extends GenericDAO<Note, Long>{
	public boolean deleteTeachersNote(Long key, String teacherToken);
	List<Note> getAll();
	List<Note> getTeachersNotes(String teacherToken);
//	public boolean safeCreateWithConfirm(Note note);
}
