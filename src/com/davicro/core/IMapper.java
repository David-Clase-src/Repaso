package com.davicro.core;

/**
 * Maps from one object to another (creating the return type directly)
 * @param <T> Source type
 * @param <O> Return type
 */
public interface IMapper<T, R> {
	R mapFrom(T source);
}
