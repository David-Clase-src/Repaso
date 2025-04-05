package com.davicro.core.serialization.serializers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.davicro.core.mapper.IDataMapper;
import com.davicro.core.mapper.IMapper;
import com.davicro.core.serialization.DeserializationException;
import com.davicro.core.serialization.ISerializer;

/**
 * Serialize an object's data into a text file using <code>PrintWriter</code> and mappers.
 * @param <T> The type of object to save and load
 */
public class StringSerializer<T> implements ISerializer<T>{
	
	//Writes from the source object to the output writer
	private IDataMapper<T, PrintWriter> saveMapper;
	
	//Creates a new object from the reader
	private IMapper<BufferedReader, T> loadMapper;
	
	public StringSerializer(IDataMapper<T, PrintWriter> saveMapper, IMapper<BufferedReader, T> loadMapper) {
		super();
		this.loadMapper = loadMapper;
		this.saveMapper = saveMapper;
	}

	@Override
	public void save(String filepath, T object) throws IOException {
		File file = new File(filepath);
		file.createNewFile();
		
		try(PrintWriter writer = new PrintWriter(file)){
			saveMapper.map(object, writer);
		} catch(IOException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public T load(String filepath) throws IOException, DeserializationException {
		File file = new File(filepath);
		try(BufferedReader reader = new BufferedReader(new FileReader(file))){
			return loadMapper.mapFrom(reader);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new DeserializationException("Could not map from reader to target object", e);
		}
	}

}
