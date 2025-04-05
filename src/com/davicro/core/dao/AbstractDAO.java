package com.davicro.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractDAO<T> implements IDAO<T> {
	private final Connection connection;
	
	public AbstractDAO(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public List<T> getAll() throws DataAccessException {
		List<T> result = new ArrayList<T>();

		try(PreparedStatement statement = connection.prepareStatement(getSelectAllQuery())){
			ResultSet rs = statement.executeQuery();
			result = mapResultSet(rs);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		
		return result;
	}

	@Override
	public T get(long id) throws DataAccessException {
		try(PreparedStatement statement = connection.prepareStatement(getSelectQuery())) {
			setId(statement, id);
			ResultSet rs = statement.executeQuery();
			List<T> list = mapResultSet(rs);
			
			return list.size() > 0 ? list.get(0) : null; //Return the first found result (or null)
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	public void save(T obj) throws DataAccessException {
		try(PreparedStatement statement = connection.prepareStatement(getInsertQuery())) {
			setInsertParameters(statement, obj);
			statement.executeUpdate();
		} catch(SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	public void update(T obj) throws DataAccessException {
		try(PreparedStatement statement = connection.prepareStatement(getUpdateQuery())) {
			setUpdateParameters(statement, obj);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	public void delete(T obj) throws DataAccessException {
		try(PreparedStatement statement = connection.prepareStatement(getDeleteQuery())) {
			setId(statement, obj);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}
	
	/**
	 * Maps a result set to a list of mapped objects
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<T> mapResultSet(ResultSet rs) throws SQLException {
		List<T> list = new ArrayList<T>();
		while(rs.next()) {
			list.add(mapRow(rs));
		}
		
		return list;
	}
	
	protected abstract T mapRow(ResultSet rs) throws SQLException;
	protected abstract void setId(PreparedStatement statement, T obj) throws SQLException;
	protected abstract void setId(PreparedStatement statement, long id) throws SQLException;
	protected abstract void setUpdateParameters(PreparedStatement statement, T obj) throws SQLException;
	protected abstract void setInsertParameters(PreparedStatement statement, T obj) throws SQLException;
	
	protected abstract String getSelectAllQuery();
	protected abstract String getSelectQuery();
	protected abstract String getUpdateQuery();
	protected abstract String getInsertQuery();
	protected abstract String getDeleteQuery();
}
