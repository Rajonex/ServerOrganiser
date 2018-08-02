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
import sends.Group;
import sends.MiniGroup;
import service.GroupService;

@Path("/group")
public class GroupRest {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack addGroup(Group group) {
		GroupService groupService = new GroupService();
		boolean result = groupService.addGroup(group);
		return new Ack("Msg", result);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Group getGroup(@QueryParam("id") long id, @QueryParam("token") String token) {
		GroupService groupService = new GroupService();
		Group result = groupService.getGroupById(id, token);
		return result;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack updateGroup(Group group)
	{
		GroupService groupService = new GroupService();
		boolean result = groupService.updateGroup(group);
		
		return new Ack("Msg", result);
	}

	@Path("/{token}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MiniGroup> getMiniGroups(@PathParam("token") String token) {
		GroupService groupService = new GroupService();
		return groupService.getMiniGroups(token);
	}
	
	
}
