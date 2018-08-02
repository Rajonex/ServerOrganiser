package dao;

import java.util.List;

import sends.Teacher;

public interface TeacherDAO extends GenericDAO<Teacher, Long>{

	
	public List<Teacher> getAll();
	public Teacher readByNameAndPassword(String name, String password);
	public Teacher readByToken(String token);
//	public Teacher readByName(String name);
//	@SuppressWarnings("deprecation")
	public boolean isExistsTeacherWithLogin(String login);
	public boolean isExistsTeacherWithToken(String token);
	
}
