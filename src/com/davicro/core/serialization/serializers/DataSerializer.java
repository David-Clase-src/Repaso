package com.davicro.core.serialization.serializers;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.davicro.core.IMapper;
import com.davicro.core.serialization.DeserializationException;
import com.davicro.core.serialization.IDataMapper;
import com.davicro.core.serialization.ISerializer;

/**
 * Serialize an object's data (primitive types) into a file using <code>DataOutputStream</code>
 * @param <T> The object to save and load
 */
public class DataSerializer<T> implements ISerializer<T> {
	private IDataMapper<T, DataOutput> dataMapper;
	private IMapper<DataInput, T> objectMapper;
	
	public DataSerializer(IDataMapper<T, DataOutput> dataMapper, IMapper<DataInput, T> objectMapper) {
		this.dataMapper = dataMapper;
		this.objectMapper = objectMapper;
	}

	@Override
	public void save(String filepath, T object) throws IOException {
		File file = new File(filepath);
		file.createNewFile();
		
		try(DataOutputStream stream = new DataOutputStream(new FileOutputStream(file))) {
			dataMapper.map(object, stream);
		}
	}

	@Override
	public T load(String filepath) throws IOException, DeserializationException {
		File file = new File(filepath);
		
		try(DataInputStream stream = new DataInputStream(new FileInputStream(file))){
			return objectMapper.mapFrom(stream);
		}
	}

}
