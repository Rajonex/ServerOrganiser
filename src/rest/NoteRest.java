package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import sends.Ack;
import sends.Note;
import service.NoteService;


@Path("/note")
public class NoteRest {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack addNote(Note note) {
		NoteService noteService = new NoteService();
		boolean confirm = noteService.addNote(note);
		

		return new Ack("Msg", confirm); // do zmiany napis
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Note> getTeachersNotes(@QueryParam("token") String token) {
		NoteService noteService = new NoteService();
		return noteService.getTeachersNotes(token);		
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ack deleteNote(@QueryParam("id") long id, @QueryParam("token") String token) {
		NoteService noteService = new NoteService();
		boolean confirm = noteService.deleteNote(id, token);
		

		return new Ack("Msg", confirm); // do zmiany napis
	}

}
