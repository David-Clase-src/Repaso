package com.davicro.test.serialization.serializers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.davicro.core.serialization.DeserializationException;
import com.davicro.core.serialization.ISerializer;
import com.davicro.core.serialization.serializers.StringSerializer;

class StringSerializerTests {

	private static final String FILEPATH = "person.txt";
	private static final Person PERSON = new Person("Guy", 10, 1.1f);
	private static ISerializer<Person> serializer;
	
	@BeforeAll
	static void init() {
		serializer = new StringSerializer<Person>(StringSerializerTests::savePerson, 
				StringSerializerTests::loadPerson);
	}
	
	private static void savePerson(Person person, PrintWriter output) throws IOException {
		output.println(person.name());
		output.println(person.age());
		output.println(person.money());
	}
	
	private static Person loadPerson(BufferedReader input) throws IOException {
		return new Person(input.readLine(), Integer.parseInt(input.readLine()), 
				Float.parseFloat(input.readLine()));
	}
	
	@Test
	void SaveTest() throws IOException {
		serializer.save(FILEPATH, PERSON);
	}
	
	@Test
	void LoadTest() throws IOException, DeserializationException{
		Person loaded = serializer.load(FILEPATH);
		
		assertEquals(PERSON, loaded);
	}
	
	@Test
	void FileNotFoundTest() {
		assertThrows(FileNotFoundException.class, () -> serializer.load("not_person"));
	}
	
	@Test
	void DeserializationExceptionTest() throws IOException {
		ISerializer<String> stringSerializer = new StringSerializer<String>(
				(String str, PrintWriter out) -> out.println(str),
				(BufferedReader in) -> in.readLine()
		);
		
		stringSerializer.save("string.txt", "Hello");
		assertThrows(DeserializationException.class, () -> serializer.load("string.txt"));
	}

}
