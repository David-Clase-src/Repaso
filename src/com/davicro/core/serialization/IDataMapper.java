package com.davicro.core.serialization;

public interface IDataMapper<T, R> {
	void map(T source, R target);
}
