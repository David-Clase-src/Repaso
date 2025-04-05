package repaso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.davicro.core.dao.DataAccessException;
import com.davicro.core.dao.IDAO;

import repaso.dto.Actor;

public final class ActorDAO implements IDAO<Actor> {
	private static final String SELECT_ALL_QUERY = "SELECT * FROM ACTOR;";
	private static final String SELECT_WITH_ID_QUERY = "SELECT * FROM ACTOR WHERE actor_id=?";
	private static final String INSERT_QUERY = "INSERT INTO ACTOR(actor_id, first_name, last_name) VALUES (?,?,?)";
	private static final String UPDATE_QUERY = "UPDATE ACTOR SET first_name=?,last_name=? WHERE actor_id=?";
	private static final String DELETE_QUERY = "DELETE FROM ACTOR WHERE actor_id=?";
	
	private final Connection connection;
	
	public ActorDAO(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public List<Actor> getAll() throws DataAccessException{
		List<Actor> actors = new ArrayList<Actor>();
		try(PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY);
				ResultSet rs = statement.executeQuery()){
			
			while(rs.next()) {
				Actor actor = mapActor(rs);
				actors.add(actor);
			}
		} catch(SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return actors;
	}

	@Override
	public Actor get(long id) throws DataAccessException {
		try(PreparedStatement statement = connection.prepareStatement(SELECT_WITH_ID_QUERY);){
			statement.setInt(1, (int)id);
			
			List<Actor> actors = DAOUtils.mapResultSet(statement.executeQuery(), this::mapActor);
			
			if(actors.size() == 0)
				return null;
			else
				return actors.get(0);
		} catch(SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	public void save(Actor obj) throws DataAccessException {
		try(PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)){
			statement.setInt(1, obj.actor_id());
			statement.setString(2, obj.first_name());
			statement.setString(3, obj.last_name());
			
			statement.executeUpdate();
		} catch(SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	public void update(Actor obj) throws DataAccessException {
		try(PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)){
			statement.setString(1, obj.first_name());
			statement.setString(2, obj.last_name());
			statement.setInt(3, obj.actor_id());
			
			statement.executeUpdate();
		} catch(SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	public void delete(Actor obj) throws DataAccessException {
		try(PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)){
			statement.setInt(1, obj.actor_id());
			
			statement.executeUpdate();
		} catch(SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	private Actor mapActor(ResultSet rs){
		try {
			return new Actor(rs.getInt("actor_id"), rs.getString("first_name"), rs.getString("last_name"),
					rs.getTimestamp("last_update"));
		} catch (SQLException e) {
			return null;
		}

	}
}
