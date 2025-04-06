package com.davicro.core.dao;

import java.util.List;

/**
 * Base interface for any Data Access Operation Object
 * @param <T> The type of object to work with
 * @param <TID> The type of id to filter tables with
 */
public interface IDAO<T, TID> {
	public List<T> getAll() throws DataAccessException;
	T get(TID id) throws DataAccessException;
	void save(T obj) throws DataAccessException;
	void update(T obj) throws DataAccessException;
	void delete(T obj) throws DataAccessException;
}
