package service;

import java.util.List;

import dao.CalendarDAO;
import dao.DAOFactory;
import sendutils.GroupCalendar;
import utils.Day;

public class CalendarService {

	public List<GroupCalendar> getTeacherCalendar(String token)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		CalendarDAO calendarDao = factory.getCalendarDAO();
		return calendarDao.getCalendarByToken(token);
	}
	
	public List<GroupCalendar> getCalendarForDay(String token, Day day)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		CalendarDAO calendarDao = factory.getCalendarDAO();
		return calendarDao.getCalendarByDate(day, token);
	}
}
