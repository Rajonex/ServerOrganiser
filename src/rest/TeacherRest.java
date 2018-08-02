package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import result.ResultTeacher;
import sends.Teacher;
import service.TeacherService;

@Path("/teacher")
public class TeacherRest {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Teacher sayHello(@QueryParam("name") String name, @QueryParam("password") String password) {
		TeacherService ts = new TeacherService();
		Teacher result = ts.login(name, password);
		return result;		
	}

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResultTeacher save(Teacher teacher) {
		TeacherService ts = new TeacherService();	
		return ts.addTeacher(teacher);
	}
}
