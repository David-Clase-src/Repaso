package com.davicro.core.serialization;

/**
 * Map the data from one existing object to another
 * @param <T>
 * @param <R>
 */
public interface IDataMapper<T, R> {
	void map(T source, R target);
}
