package com.pigol.cassandra.app.dao;

import java.util.List;

import com.google.common.base.Preconditions;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.pigol.cassandra.app.db.Connection;
import com.pigol.cassandra.app.db.DbConfig;
import com.pigol.cassandra.app.models.InventoryItem;

public class InventoryItemDao {

	private DbConfig config;
	
	private Connection conn;
	
	public InventoryItemDao(DbConfig config){
		config = config;
		conn = new Connection(config);
		conn.open(config.getKeyspace());
	}

	private static final class InventoryFields{
		public static final String SKU = "sku";		
		public static final String PRICE = "price";
		public static final String ATTRIBUTES = "attributes";
	}
	
	
	public InventoryItem getItemBySku(String sku){
		
		Preconditions.checkNotNull(sku);
		Preconditions.checkArgument(!sku.isEmpty());
		
		String cql = "SELECT * FROM inventory WHERE sku = '" + sku + "'";
		ResultSet rs = conn.execute(cql);
		if(!rs.isExhausted()){
			Row r = rs.one();
			InventoryItem item = new InventoryItem();
			item.setSku(r.getString(InventoryFields.SKU));
			item.setPrice(r.getDouble(InventoryFields.PRICE));
			item.setAttributesFromMap(r.getMap(InventoryFields.ATTRIBUTES, String.class, String.class));
			return item;
		}else{
			InventoryItem item = new InventoryItem();
			item.setEmpty();
			return item;
		}
	}
	
	public List<InventoryItem> getItemsInRange(Double lower, Double upper){
		
		Preconditions.checkArgument(upper.compareTo(lower) > 0);
		Preconditions.checkArgument(upper > 0);
		Preconditions.checkArgument(lower > 0);
		Preconditions.checkNotNull(upper);
		Preconditions.checkNotNull(lower);
		
		
		
		return null;
	}
	
	
	public List<InventoryItem> getCheaperItems(Double upper){
	
		Preconditions.checkArgument(upper > 0);
		Preconditions.checkNotNull(upper);
		
		return null;
	}
	
	public List<InventoryItem> getExpensiveItems(Double lower){
		
		Preconditions.checkNotNull(lower);
		Preconditions.checkArgument(lower > 0);
		
		return null;
	}
	
}
