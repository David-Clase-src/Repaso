package com.davicro.test.serialization.serializers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.davicro.core.serialization.DeserializationException;
import com.davicro.core.serialization.ISerializer;
import com.davicro.core.serialization.serializers.BinarySerializer;

class BinarySerializerTests {
	private static final String FILEPATH = "person";
	private static final Person person = new Person("Pat", 10, 1.1f);
	private static final ISerializer<Person> serializer = new BinarySerializer<>(Person.class);
	
	@Test
	void SaveTest() throws IOException {
		serializer.save(FILEPATH, person);
	}
	
	@Test
	void LoadTest() throws IOException, DeserializationException {
		Person loaded = serializer.load(FILEPATH);

		assertEquals(person, loaded);
	}
	
	
	@Test
	void FileNotFoundTest() {
		assertThrows(FileNotFoundException.class, () -> serializer.load("not_person"));
	}
	
	@Test
	void CastExceptionTest() throws IOException {
		ISerializer<String> stringSerializer = new BinarySerializer<>(String.class);
		stringSerializer.save("string.txt", "Hello");
		assertThrows(DeserializationException.class, () -> serializer.load("string.txt"));
	}
	
	private static record Person(String name, int age, float money) implements Serializable {
	}
}
