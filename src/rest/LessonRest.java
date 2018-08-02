package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import sends.Ack;
import sends.Lesson;
import sends.MiniLesson;
import service.LessonService;

@Path("/lesson")
public class LessonRest {
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MiniLesson> getGroupLessons(@QueryParam("groupid") long id, @QueryParam("token") String token)
	{
		LessonService lessonService = new LessonService();
		return lessonService.getMiniLessons(id, token);
	}
	
	
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Lesson getLessonById(@PathParam("id") long id, @QueryParam("token") String token)
	{
//		System.out.println("Wchodzi");
		LessonService lessonService = new LessonService();
		return lessonService.getLessonById(id, token);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack updateLesson(Lesson lesson)
	{
		LessonService lessonService = new LessonService();
		boolean result = lessonService.updateLesson(lesson);
		
		return new Ack("Msg", result);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack setLesson(Lesson lesson)
	{
		LessonService lessonService = new LessonService();
		return lessonService.addLesson(lesson);
	}
}
