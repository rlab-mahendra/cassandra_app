package com.pigol.cassandra.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigol.cassandra.app.dao.InventoryItemDao;
import com.pigol.cassandra.app.db.DbConfig;


public class InventoryApp {

	private static final Logger LOG = LoggerFactory.getLogger(InventoryApp.class);
	
	public void run(DbConfig config){
		
		InventoryItemDao dao = new InventoryItemDao(config);
		System.out.println(dao.getItemBySku("1212121"));
	}
	
	public static void main(String[] args) {
	   	DbConfig c = new DbConfig("192.168.56.101");
	   	c.setKeyspace("mykeyspace");
	   	InventoryApp app = new InventoryApp();
	   	app.run(c);
	}
}