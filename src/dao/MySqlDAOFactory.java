package dao;

public class MySqlDAOFactory extends DAOFactory {

	@Override
	public CalendarDAO getCalendarDAO() {
		return new CalendarDAOImpl();
	}

	@Override
	public GroupDAO getGroupDAO() {
		return new GroupDAOImpl();
	}

	@Override
	public LessonDAO getLessonDAO() {
		return new LessonDAOImpl();
	}

	@Override
	public NoteDAO getNoteDAO() {
		return new NoteDAOImpl();
	}

	@Override
	public SaldoDAO getSaldoDAO() {
		return new SaldoDAOImpl();
	}

	@Override
	public StudentDAO getStudentDAO() {
		return new StudentDAOImpl();
	}

	@Override
	public TeacherDAO getTeacherDAO() {
		return new TeacherDAOImpl();
	}

	
	
}
