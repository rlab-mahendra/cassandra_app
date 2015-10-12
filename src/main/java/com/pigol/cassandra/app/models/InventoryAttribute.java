package com.pigol.cassandra.app.models;

public class InventoryAttribute {

	@Override
	public String toString() {
		return "InventoryAttribute [name=" + name + ", value=" + value + "]";
	}

	private String name;
	
	private String value;

	public InventoryAttribute(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
