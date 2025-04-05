package com.davicro.core.mapper;

/**
 * Map the data from one existing object to another
 * @param <T>
 * @param <R>
 */
public interface IDataMapper<T, R> {
	/*
	 * Throws exception is very vague, but it needs to be since mappers could be anything,
	 * from files to json to any two objects, so I guess it's necessary.
	 */
	void map(T source, R target) throws Exception;
}
