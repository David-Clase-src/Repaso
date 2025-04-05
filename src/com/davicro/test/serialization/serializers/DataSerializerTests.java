package com.davicro.test.serialization.serializers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.davicro.core.serialization.DeserializationException;
import com.davicro.core.serialization.ISerializer;
import com.davicro.core.serialization.serializers.BinarySerializer;
import com.davicro.core.serialization.serializers.DataSerializer;

class DataSerializerTests {
	private static final String FILEPATH = "person.data";
	private static final Person PERSON = new Person("Dude", 10, 1.1f);
	private static ISerializer<Person> serializer;
	
	@BeforeAll
	static void init() {
		serializer = new DataSerializer<Person>(DataSerializerTests::savePerson, 
				DataSerializerTests::loadPerson);
	}
	
	private static void savePerson(Person person, DataOutput output) throws IOException {
		output.writeUTF(person.name());
		output.writeInt(person.age());
		output.writeFloat(person.money());
	}
	
	private static Person loadPerson(DataInput input) throws IOException {
		return new Person(input.readUTF(), input.readInt(), input.readFloat());
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
		ISerializer<String> stringSerializer = new DataSerializer<>(
				(String str, DataOutput out) -> out.writeUTF(str),
				(DataInput in) -> in.readUTF()
		);
		
		stringSerializer.save("string.data", "Hello");
		assertThrows(IOException.class, () -> serializer.load("string.data"));
	}

}
