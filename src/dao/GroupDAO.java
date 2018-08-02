package dao;

import java.util.List;

import sends.Group;
import sends.MiniGroup;

public interface GroupDAO extends GenericDAO<Group, Long> {

	public List<Group> getAll();
	public List<MiniGroup> getTeachersMiniGroups(String token);
	public Group getGroupByIdAndToken(long id, String token);
	public boolean deleteStudentGroup(long groupId);
	public boolean addStudentGroup(long studentId, long groupId);
}
