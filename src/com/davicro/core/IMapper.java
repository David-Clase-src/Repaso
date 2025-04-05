package com.davicro.core;

/**
 * Map from one object to another
 * @param <T> Source type
 * @param <O> Return type
 */
public interface IMapper<T, R> {
	R mapFrom(T source);
}
