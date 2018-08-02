package service;

import java.util.List;

import dao.CalendarDAO;
import dao.DAOFactory;
import dao.GroupDAO;
import dao.StudentDAO;
import sends.Group;
import sends.MiniGroup;
import sends.Student;
import sendutils.GroupCalendar;

public class GroupService {

	public boolean addGroup(Group group)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		GroupDAO groupDao = factory.getGroupDAO();
		return groupDao.create(group);
	}
	
	public Group getGroupById(long groupId, String token)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		GroupDAO groupDao = factory.getGroupDAO();
		CalendarDAO calendarDao = factory.getCalendarDAO();
		StudentDAO studentDao = factory.getStudentDAO();
		
		Group resultGroup = groupDao.getGroupByIdAndToken(groupId, token);
		if(resultGroup != null)
		{
			resultGroup.setGroupCalendar(calendarDao.getGroupsCalendars(groupId));
			
			resultGroup.setStudents(studentDao.getByGroup(groupId, resultGroup.getTeacherToken()));
		}
		
		return resultGroup;
	}
	
	public boolean updateGroup(Group group)
	{
		boolean result = false;
		DAOFactory factory = DAOFactory.getDAOFactory();
		GroupDAO groupDao = factory.getGroupDAO();
		CalendarDAO calendarDao = factory.getCalendarDAO();
		if(groupDao.update(group))
		{
			long groupId = group.getId();
			result = true;
			List<Student> students = group.getStudents();
			groupDao.deleteStudentGroup(groupId);
			if(students!=null)
			{
				for(Student student: students)
				{
					groupDao.addStudentGroup(student.getId(), groupId);
				}
			}
			List<GroupCalendar> calendars = group.getGroupCalendar();
			calendarDao.deleteGroupCalendar(groupId);
			if(calendars != null)
			{
				for(GroupCalendar calendar: calendars)
				{
					calendarDao.addGroupCalendar(calendar, groupId);
				}
			}
		}
		
		return result;
	}
	
	public List<MiniGroup> getMiniGroups(String token)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		GroupDAO groupDao = factory.getGroupDAO();
		return groupDao.getTeachersMiniGroups(token);
	}
}
