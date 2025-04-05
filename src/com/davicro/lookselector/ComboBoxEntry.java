package com.davicro.lookselector;

public class ComboBoxEntry<T> {
	private String name;
	private T obj;
	
	public ComboBoxEntry(String name, T obj) {
		this.name = name;
		this.obj = obj;
	}
	
	public String getName() {
		return name;
	}

	public T getObj() {
		return obj;
	}

	@Override
	public String toString() {
		return getName();
	}
}
