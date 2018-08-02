package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import sends.Ack;
import sends.Saldo;
import service.SaldoService;

@Path("/saldo")
public class SaldoRest
{
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Saldo> getSaldo(@QueryParam("token") String token, @QueryParam("month") int month, @QueryParam("year") int year, @QueryParam("groupId") long groupId) {
		SaldoService saldoService = new SaldoService();
		return saldoService.getSaldoByGroupAndDate(month, year, token, groupId);
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Ack updateSaldo(Saldo saldo)
	{
		SaldoService saldoService = new SaldoService();
		boolean result = saldoService.updatePaid(saldo);
		
		return new Ack("Msg", result);
	}
}
