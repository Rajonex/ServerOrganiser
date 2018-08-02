package service;

import java.util.List;

import dao.DAOFactory;
import dao.NoteDAO;
import sends.Note;

public class NoteService {

	/**
	 * 
	 * @param note 
	 * @return information about success of adding note
	 */
	public boolean addNote(Note note)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		NoteDAO noteDao = factory.getNoteDAO();
		return noteDao.create(note);
	}
	
	public List<Note> getTeachersNotes(String teacherToken)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		NoteDAO noteDao = factory.getNoteDAO();
		return noteDao.getTeachersNotes(teacherToken);
	}
	
	public boolean deleteNote(long id, String token)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		NoteDAO noteDao = factory.getNoteDAO();
		return noteDao.deleteTeachersNote(id, token);
	}

}
