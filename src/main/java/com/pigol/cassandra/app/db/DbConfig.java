package com.pigol.cassandra.app.db;

import java.util.List;

import com.google.common.collect.Lists;

public class DbConfig {

	private List<String> hosts = Lists.newArrayList();

	private String keyspace;
	
	public DbConfig(List<String> hosts){
		this.hosts.addAll(hosts);		
	}

	public DbConfig(String host){
		this.hosts.add(host);
	}
	
	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	public List<String> getHosts(){
		return this.hosts;
	}
	
	
	public void addHost(String host){
		this.hosts.add(host);
	}
	
}
