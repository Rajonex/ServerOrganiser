package rest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import sends.Ack;
import sends.Lesson;
import sends.Student;
import sendutils.StudentPresent;
import service.LessonService;

@Path("/hello")
public class TestHello {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ack> sayHello(/*@QueryParam("name") String name, @QueryParam("number") int number*/) {
		List<Ack> acks = new ArrayList<Ack>();
		

		

		Student student = new Student(1L, "Jarek", "Niemam", "0700", "jarekmail@mail.com", "e2e42a07-5508-33f8-b67f-5eb252581f6d..", true);
		StudentPresent present = new StudentPresent(19L, student, true);
		List<StudentPresent> presents = new ArrayList<>();
		presents.add(present);
		Lesson lesson = new Lesson(20L, presents, "Temat", "Opis", Date.valueOf(LocalDate.now()).getTime(), 1L, "e2e42a07-5508-33f8-b67f-5eb252581f6d..");
		LessonService lessonService = new LessonService();
		acks.add(lessonService.addLesson(lesson));
		return acks;
	}

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack save(List<Ack> user) {
		for (Ack a : user)
			System.out.println(a);
		Ack ack = new Ack("Accepted", true);
		return ack;

	}
}
