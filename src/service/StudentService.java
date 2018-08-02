package service;

import java.util.List;

import dao.DAOFactory;
import dao.StudentDAO;
import sends.Student;

public class StudentService {

	public boolean addStudent(Student student)
	{
		DAOFactory daoFactory = DAOFactory.getDAOFactory();
		StudentDAO studentDao = daoFactory.getStudentDAO();
		return studentDao.create(student);
	}
	
	public boolean updateStudent(Student student)
	{
		DAOFactory daoFactory = DAOFactory.getDAOFactory();
		StudentDAO studentDao = daoFactory.getStudentDAO();
		return studentDao.update(student);
	}
	
	public boolean updateStudentActivity(Student student)
	{
		DAOFactory daoFactory = DAOFactory.getDAOFactory();
		StudentDAO studentDao = daoFactory.getStudentDAO();
		return studentDao.updateActivity(student);
	}
	
	public List<Student> getAllStudents(String token)
	{
		DAOFactory daoFactory = DAOFactory.getDAOFactory();
		StudentDAO studentDao = daoFactory.getStudentDAO();
		return studentDao.getAllForTeacher(token);
	}
	
	public List<Student> getGroupStudents(String token, long groupId)
	{
		DAOFactory daoFactory = DAOFactory.getDAOFactory();
		StudentDAO studentDao = daoFactory.getStudentDAO();
		return studentDao.getByGroup(groupId, token);
	}
}
