package com.pigol.cassandra.app.db;

import java.util.Set;

import com.datastax.driver.core.Host;

public class ClusterConfig {

	private String name;
	
	private Set<Host> hosts;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Host> getHosts() {
		return hosts;
	}

	public void setHosts(Set<Host> hosts) {
		this.hosts = hosts;
	}	
}
