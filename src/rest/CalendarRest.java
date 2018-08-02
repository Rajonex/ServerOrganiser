package rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import sends.Group;
import sendutils.GroupCalendar;
import service.CalendarService;
import utils.Day;

@Path("/calendar")
public class CalendarRest {

	@Path("/{token}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<GroupCalendar> getTeacherCalendar(@PathParam("token") String token) {
		CalendarService calendarService = new CalendarService();
		return calendarService.getTeacherCalendar(token);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<GroupCalendar> getCalendarByDate(@QueryParam("token") String token, @QueryParam("day") String day) {
		CalendarService calendarService = new CalendarService();
		return calendarService.getCalendarForDay(token, Day.valueOf(day));
	}
}
