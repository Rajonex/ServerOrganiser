package service;

import java.util.List;

import dao.DAOFactory;
import dao.SaldoDAO;
import sends.Saldo;

public class SaldoService
{
	public List<Saldo> getSaldoByGroupAndDate(int month, int year, String token, long groupId)
	{
		DAOFactory daoFactory = DAOFactory.getDAOFactory();
		SaldoDAO saldoDao = daoFactory.getSaldoDAO();
		return saldoDao.getGroupsSaldoByDate(groupId, month, year, token);
	}
	
	public boolean updatePaid(Saldo saldo)
	{
		DAOFactory daoFactory = DAOFactory.getDAOFactory();
		SaldoDAO saldoDao = daoFactory.getSaldoDAO();
		return saldoDao.updatePaid(saldo);
	}
}
