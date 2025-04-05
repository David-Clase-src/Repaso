package com.davicro.core.dao;

import java.util.List;

public interface IDAO<T> {
	public List<T> getAll() throws DataAccessException;
	T get(long id) throws DataAccessException;
	void save(T obj) throws DataAccessException;
	void update(T obj) throws DataAccessException;
	void delete(T obj) throws DataAccessException;
}
