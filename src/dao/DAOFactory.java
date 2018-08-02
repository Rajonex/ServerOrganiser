package dao;

public abstract class DAOFactory {
	public abstract CalendarDAO getCalendarDAO();
	public abstract GroupDAO getGroupDAO();
	public abstract LessonDAO getLessonDAO();
	public abstract NoteDAO getNoteDAO();
	public abstract SaldoDAO getSaldoDAO();
	public abstract StudentDAO getStudentDAO();
	public abstract TeacherDAO getTeacherDAO();
	
	public static DAOFactory getDAOFactory()
	{
		return new MySqlDAOFactory();
	}
}
