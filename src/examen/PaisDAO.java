package examen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.davicro.core.dao.AbstractDAO;

public class PaisDAO extends AbstractDAO<Pais, String>{
	public PaisDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected Pais mapRow(ResultSet rs) throws SQLException {
		return new Pais(rs.getString("nombre"), rs.getDouble("poblacion"), -1, -1);
	}

	@Override
	protected void setIdFromObj(PreparedStatement statement, Pais obj) throws SQLException {
		statement.setString(1, obj.nombre());
	}

	@Override
	protected void setId(PreparedStatement statement, String id) throws SQLException {
		statement.setString(1, id);
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, Pais obj) throws SQLException {
	}

	@Override
	protected void setInsertParameters(PreparedStatement statement, Pais obj) throws SQLException {
		statement.setString(1, obj.nombre());
		statement.setDouble(2, obj.poblacion());
	}

	@Override
	protected String getSelectAllQuery() {
		throw new UnsupportedOperationException("Not implemented.");
	}

	@Override
	protected String getSelectQuery() {
		return "SELECT * FROM PAISES WHERE nombre = ?";
	}

	@Override
	protected String getUpdateQuery() {
		throw new UnsupportedOperationException("Not implemented.");
	}

	@Override
	protected String getInsertQuery() {
		return "INSERT INTO PAISES(nombre, poblacion) VALUES(?,?)";
	}

	@Override
	protected String getDeleteQuery() {
		throw new UnsupportedOperationException("Not implemented.");
	}

	
}
