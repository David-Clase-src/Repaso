package com.davicro.core.mapper;

import java.util.Optional;

/**
 * Map from one object to a new one
 * @param <T> Source type
 * @param <O> Return type
 */
public interface IMapper<T, R> {
	/*
	 * Throws exception is very vague, but it needs to be since mappers could be anything,
	 * from files to json to any two objects, so I guess it's necessary.
	 */
	R mapFrom(T source) throws Exception;
}
