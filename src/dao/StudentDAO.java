package dao;

import java.util.List;

import sends.Student;
import sendutils.StudentPresent;

public interface StudentDAO extends GenericDAO<Student, Long> {
	public List<Student> getAll();
	public List<Student> getAllForTeacher(String token);
	public List<Student> getByGroup(long groupId, String token);
	public List<StudentPresent> getPresenceForLesson(long lessonId);
	public boolean updateActivity(Student updateObject);
}
