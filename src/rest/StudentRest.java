package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import sends.Ack;
import sends.Student;
import service.StudentService;

@Path("/student")
public class StudentRest {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Student> sayHello(@QueryParam("token") String token) {
		StudentService studentService = new StudentService();
		return studentService.getAllStudents(token);		
	}
	
	@Path("/group")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Student> getGroupStudents(@QueryParam("token") String token, @QueryParam("groupId") long groupId)
	{
		StudentService studentService = new StudentService();
		return studentService.getGroupStudents(token, groupId);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack addStudent(Student student) {
		StudentService studentService = new StudentService();
		boolean confirm = studentService.addStudent(student);
		
		return new Ack("Msg", confirm); // do zmiany napis
	}
	
	@Path("/archive")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack archiveStudent(Student student)
	{
		StudentService studentService = new StudentService();
		boolean confirm = studentService.updateStudentActivity(student);
		
		return new Ack("Msg", confirm); // do zmiany napis
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack updateStudent(Student student) {
		StudentService studentService = new StudentService();
		boolean confirm = studentService.updateStudent(student);
		
		return new Ack("Msg", confirm); // do zmiany napis
	}
}
