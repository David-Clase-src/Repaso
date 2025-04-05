package repaso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.davicro.core.dao.AbstractDAO;

import repaso.dto.Actor;

public class ActorDAO extends AbstractDAO<Actor>{

	public ActorDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected Actor mapRow(ResultSet rs) throws SQLException {
		return new Actor(rs.getInt("actor_id"), rs.getString("first_name"), rs.getString("last_name"),
					rs.getTimestamp("last_update"));
	}

	@Override
	protected void setId(PreparedStatement statement, Actor obj) throws SQLException {
		statement.setInt(1, obj.actor_id());
	}
	
	@Override
	protected void setId(PreparedStatement statement, long id) throws SQLException {
		statement.setInt(1, (int)id);
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, Actor obj) throws SQLException {
		statement.setString(1, obj.first_name());
		statement.setString(2, obj.last_name());
		statement.setInt(3, obj.actor_id());
	}

	@Override
	protected void setInsertParameters(PreparedStatement statement, Actor obj) throws SQLException {
		statement.setInt(1, obj.actor_id());
		statement.setString(2, obj.first_name());
		statement.setString(3, obj.last_name());
	}

	@Override
	protected String getSelectAllQuery() {
		return "SELECT * FROM ACTOR";
	}

	@Override
	protected String getSelectQuery() {
		return "SELECT * FROM ACTOR WHERE actor_id = ?";
	}

	@Override
	protected String getUpdateQuery() {
		return "UPDATE ACTOR SET first_name=?,last_name=? WHERE actor_id=?";
	}

	@Override
	protected String getInsertQuery() {
		return "INSERT INTO ACTOR(actor_id, first_name, last_name) VALUES (?,?,?)";
	}

	@Override
	protected String getDeleteQuery() {
		return "DELETE FROM ACTOR WHERE actor_id=?";
	}
}
