package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BasicDao {

	private Connection connection;
	
	public BasicDao(String dbAddress) {
		super();
		try {
			connection = DriverManager.getConnection(dbAddress);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Verificare nel build la presenza del connector ");
			System.out.println("Verificare dbAddress, username e la password ");
		}
	}
	
	
	public List<Map<String, String>> getAll(String sql, Object... conditions) {
		
		List<Map<String, String>> ris = new ArrayList<>();
		
		try {
			ResultSet rs = executeQuery(sql, conditions);
			
			while (rs.next()) {
				Map<String, String> map = mapFromRS(rs);
				ris.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ris;
	}

	private ResultSet executeQuery(String sql, Object... conditions) throws SQLException {
		return preparedStm(sql, conditions).executeQuery();
	}

	
	private PreparedStatement preparedStm(String sql, Object... conditions) throws SQLException {
		PreparedStatement stm = connection.prepareStatement(sql);
		
		for (int i = 0; i < conditions.length; i++) {
			stm.setObject(i + 1, conditions[i]);
		}
		
		return stm;
	}

	
	private Map<String, String> mapFromRS(ResultSet rs) throws SQLException {
		Map<String, String> map = new HashMap<>();
		
		ResultSetMetaData meta = rs.getMetaData();
		
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			map.put(meta.getColumnName(i), rs.getString(i));
		}
		
		return map;
	}
	
	public Map<String, String> getOne(String sql, Object... conditions) {
		Map<String, String> ris = null;
		
		try {
			ResultSet rs = executeQuery(sql, conditions);
			
			if (rs.next()) {
				ris = mapFromRS(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ris;
	}
	
	public void execute(String sql, Object... conditions) {
		try {
			preparedStm(sql, conditions).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}