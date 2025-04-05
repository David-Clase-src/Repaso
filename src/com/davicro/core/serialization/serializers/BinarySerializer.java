package com.davicro.core.serialization.serializers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.davicro.core.serialization.DeserializationException;
import com.davicro.core.serialization.ISerializer;

/**
 * Serialize an object into a binary file using <code>ObjectOutputStream</code>
 * @param <T> The type of object to save and load
 */
public class BinarySerializer<T> implements ISerializer<T>{
	private Class<T> type;
	
	public BinarySerializer(Class<T> classType) {
		this.type = classType;
	}
	
	@Override
	public void save(String filepath, T obj) throws IOException {
		File file = new File(filepath);
		file.createNewFile(); //Creates the file only if it does not exist yet
		
		try(ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))){
			stream.writeObject(obj);
		}
	}

	@Override
	public T load(String filepath) throws IOException, DeserializationException {
		File file = new File(filepath);
		
		//Deserialize the target file and cast it into the required type
		try(ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file))) {
			Object obj = stream.readObject();
			
			return type.cast(obj);
		} catch (ClassNotFoundException e) {
			//If the class is not found, wrap it as a generic deserialization exception for the owner to handle
			throw new DeserializationException(e.getMessage(), e);
		} catch(ClassCastException e) {
			//Same goes for a class cast exception, if we can't cast it, we can't really do much
			throw new DeserializationException(e.getMessage(), e);
		}
	}
	
}
