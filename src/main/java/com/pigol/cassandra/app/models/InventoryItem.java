package com.pigol.cassandra.app.models;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class InventoryItem {

	@Override
	public String toString() {
		return "InventoryItem [sku=" + sku + ", price=" + price + ", brandId="
				+ brandId + ", attributes=" + attributes + "]";
	}

	private String sku;
	
	private Double price;
	
	private Integer brandId;

	private Map<String, InventoryAttribute> attributes = Maps.newHashMap();

	private boolean empty = false;
	
	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Map<String, InventoryAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, InventoryAttribute> attributes) {
		this.attributes.putAll(attributes);
	}
	
	public void setAttributesFromMap(Map<String, String> attributes){
		for(Entry<String, String> keySet : attributes.entrySet()){
			this.attributes.put(keySet.getKey(), new InventoryAttribute(keySet.getKey(), keySet.getValue()));
		}
	}
	
	public void addAttribute(InventoryAttribute attribute){
		this.attributes.put(attribute.getName(), attribute);
	}	
	
	public void setEmpty(){
		this.empty = true;
	}
	
	public boolean isNull(){
		return this.empty;
	}
	
}
