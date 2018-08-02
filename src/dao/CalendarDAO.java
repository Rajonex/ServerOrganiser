package dao;

import java.sql.Date;
import java.util.List;

import sendutils.GroupCalendar;
import utils.Day;

public interface CalendarDAO extends GenericDAO<GroupCalendar, Long>{

	public List<GroupCalendar> getCalendarByToken(String token);
	public List<GroupCalendar> getCalendarByDate(Day day, String token);
	public List<GroupCalendar> getGroupsCalendars(long groupId);
	public List<GroupCalendar> getGroupsCalendarByDate(long groupId, Date date);
	public boolean addGroupCalendar(GroupCalendar calendar, long groupId);
	public boolean deleteGroupCalendar(long groupId);
}
