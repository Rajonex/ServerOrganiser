package service;

import java.util.UUID;

import dao.DAOFactory;
import dao.TeacherDAO;
import result.ResultTeacher;
import sends.Teacher;

public class TeacherService {
	
	public TeacherService() {}

	/**
	 * 
	 * @param teacher
	 * @return 0 if add succesfull, 1 if name exists in database, 2 if can't create token, 3 if other error
	 *         token
	 */
	public ResultTeacher addTeacher(Teacher teacher) {
		DAOFactory factory = DAOFactory.getDAOFactory();
		TeacherDAO teacherDao = factory.getTeacherDAO();
		if (teacherDao.isExistsTeacherWithLogin(teacher.getLogin())) {
			return new ResultTeacher(1, null);
		}
		UUID token = UUID.nameUUIDFromBytes(teacher.getLogin().getBytes());
		String strToken = token.toString();
		if (teacherDao.isExistsTeacherWithToken(strToken)) {
			boolean flag = false; // is successfull create token
			for (int i = 0; i < 20; i++) {
				token = UUID.randomUUID();
				strToken = token.toString();
				System.out.println(strToken);
				if (!teacherDao.isExistsTeacherWithToken(strToken)) {
					flag = true;
					break;
				}
			}
			if (!flag) // if can't create token
			{
				return new ResultTeacher(2, null);
			}
		}
//		System.out.println(strToken);
		teacher.setToken(strToken);
		if(teacherDao.create(teacher))
			return new ResultTeacher(0, teacher);
		return new ResultTeacher(3, null);
	}
	
	public Teacher login(String name, String password)
	{
		Teacher resTeacher = null;
		DAOFactory factory = DAOFactory.getDAOFactory();
		TeacherDAO teacherDao = factory.getTeacherDAO();
		resTeacher = teacherDao.readByNameAndPassword(name, password);
		return resTeacher;
	}
	
//	public Teacher readTeacherById()
//	{
//		Teacher resTeacher = null;
//		DAOFactory factory = DAOFactory.getDAOFactory();
//		TeacherDAO teacherDao = factory.getTeacherDAO();
//		resTeacher = teacherDao.read(primaryKey)
//	}
}
