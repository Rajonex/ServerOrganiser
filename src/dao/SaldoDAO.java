package dao;

import java.sql.Date;
import java.util.List;

import sends.Saldo;

public interface SaldoDAO extends GenericDAO<Saldo, Long> {
	public List<Saldo> getAll();
	public List<Saldo> getGroupsSaldo(long groupId);
	public List<Saldo> getByDate(Date date);
	public List<Saldo> getGroupsSaldoByDate(long groupId, int month, int year, String token);
	public boolean updatePaid(Saldo saldo);
}
