package com.davicro.core.serialization;

import java.io.IOException;

public interface ISerializer<T> {
	void save(String filepath, T object) throws IOException;
	T load(String filepath) throws IOException, DeserializationException;
}
