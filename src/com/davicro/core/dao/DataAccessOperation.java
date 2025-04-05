package com.davicro.core.dao;

import java.sql.SQLException;

/**
 * A Data Access Operation on an existing object in the database
 * @param <T>
 */
public interface DataAccessOperation<T> {
	boolean execute(T obj) throws DataAccessException;
}
