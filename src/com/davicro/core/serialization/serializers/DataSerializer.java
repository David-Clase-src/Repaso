package com.davicro.core.serialization.serializers;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.davicro.core.mapper.IDataMapper;
import com.davicro.core.mapper.IMapper;
import com.davicro.core.serialization.DeserializationException;
import com.davicro.core.serialization.ISerializer;

/**
 * Serialize an object's primitive data types into a file using <code>DataOutputStream</code> and mappers.
 * @param <T> The type of object to save and load
 */
public class DataSerializer<T> implements ISerializer<T> {
	//Writes from the source object to the output stream
	private IDataMapper<T, DataOutput> saveMapper;
	
	//Creates a new object from the DataInput
	private IMapper<DataInput, T> loadMapper;
	
	public DataSerializer(IDataMapper<T, DataOutput> saveMapper, IMapper<DataInput, T> loadMapper) {
		this.saveMapper = saveMapper;
		this.loadMapper = loadMapper;
	}

	@Override
	public void save(String filepath, T object) throws IOException {
		File file = new File(filepath);
		file.createNewFile();
		
		try(DataOutputStream stream = new DataOutputStream(new FileOutputStream(file))) {
			saveMapper.map(object, stream);
		} catch(IOException e) {
			throw e;
		} catch (Exception e) {
			/*
			 * We kind of just consume unexpected exceptions because there's no other
			 * exception I can think of that would happen with writing to a file,
			 * unless the mapping function is very wrong.
			 */
			e.printStackTrace(); 
		}
	}

	@Override
	public T load(String filepath) throws IOException, DeserializationException {
		File file = new File(filepath);
		
		try(DataInputStream stream = new DataInputStream(new FileInputStream(file))){
			try {
				return loadMapper.mapFrom(stream);
			} catch (IOException e) {
				throw e;
			} catch(Exception e) {
				throw new DeserializationException("Failed to map object from input stream.", e);
			}
		}
	}

}
