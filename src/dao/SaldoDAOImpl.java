package dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import connection.ConnectionProvider;
import sends.Saldo;
import sends.Student;


public class SaldoDAOImpl implements SaldoDAO
{
	private static final String READ_SALDO = "SELECT student.student_id, firstname, lastname, phone, email, student_activity, teacher_token, saldo_id, group_id, lessons_number, to_pay, paid, saldo_date FROM saldo INNER JOIN student ON student.student_id = saldo.student_id WHERE teacher_token=:teacher_token AND month(saldo_date)=:month AND year(saldo_date)=:year AND group_id=:group_id;";
	private static final String UPDATE_PAID = "UPDATE saldo SET paid=:paid WHERE saldo_id=:saldo_id AND student_id=:student_id AND group_id=:group_id";
	
	private NamedParameterJdbcTemplate template;

	public SaldoDAOImpl()
	{
		template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());

	}

	@Override
	public boolean create(Saldo newObject)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Saldo read(Long primaryKey)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Saldo updateObject)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Long key)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Saldo> getAll()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Saldo> getGroupsSaldo(long groupId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Saldo> getByDate(Date date)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean updatePaid(Saldo saldo)
	{
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("group_id", saldo.getGroupId());
		paramMap.put("student_id", saldo.getStudent().getId());
		paramMap.put("saldo_id", saldo.getId());
		paramMap.put("paid", saldo.getPaid());
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		int update = template.update(UPDATE_PAID, paramSource);
		if(update > 0)
			return true;
		
		return false;
	}

	
	@Override
	public List<Saldo> getGroupsSaldoByDate(long groupId, int month, int year, String token)
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("group_id", groupId);
		paramMap.put("teacher_token", token);
		paramMap.put("year", year);
		paramMap.put("month", month);
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		List<Saldo> resultList = template.query(READ_SALDO, paramSource, new SaldoRowMapper());
		return resultList;
	}
	
	private class SaldoRowMapper implements RowMapper<Saldo>{

		@Override
		public Saldo mapRow(ResultSet arg0, int arg1) throws SQLException
		{
			Student student = new Student();
			student.setActivity(arg0.getBoolean("student_activity"));
			student.setEmail(arg0.getString("email"));
			student.setFirstname(arg0.getString("firstname"));
			student.setId(arg0.getLong("student_id"));
			student.setLastname(arg0.getString("lastname"));
			student.setPhone(arg0.getString("phone"));
			student.setTeacherToken(arg0.getString("teacher_token"));
			Saldo saldo = new Saldo();
			saldo.setDate(arg0.getDate("saldo_date").getTime());
			saldo.setGroupId(arg0.getLong("group_id"));
			saldo.setId(arg0.getLong("saldo_id"));
			saldo.setLessonsNumber(arg0.getInt("lessons_number"));
			saldo.setPaid(arg0.getDouble("paid"));
			saldo.setStudent(student);
			saldo.setToPay(arg0.getDouble("to_pay"));
			return saldo;
		}
		
	}

}
