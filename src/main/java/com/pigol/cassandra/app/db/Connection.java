package com.pigol.cassandra.app.db;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;

public class Connection implements Closeable{
	
	private DbConfig conf;
	
	private Cluster cluster;
		
	private Session session;
	
	private String keyspace;
	
	public Connection(DbConfig conf){
		this.conf = conf;
	}

	public void open(String keyspace){
		
		this.keyspace = keyspace;
		Cluster.Builder b = Cluster.builder();
		for(String host : conf.getHosts()){
			b.addContactPoint(host);
		}
		
		cluster = b.build();
		session = cluster.connect(keyspace);
	}
	
	public ResultSet execute(String cql){
		
		if(!session.isClosed()){
			ResultSet rs = session.execute(cql);
			return rs;
		}else{
			session = cluster.connect(keyspace);
			return session.execute(cql);
		}
	}
	
	public ResultSet executeDelayed(String cql) throws InterruptedException, ExecutionException{
		
		if(session.isClosed()){
			session = cluster.connect(keyspace);
		}
		
		ResultSetFuture f = session.executeAsync(cql);
		
		while(!f.isDone()){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ResultSet rs = f.get();
		return rs;
	}
	
	public ClusterConfig getClusterInfo(){
		
		if(!cluster.isClosed()){
			ClusterConfig c = new ClusterConfig();
			c.setHosts(cluster.getMetadata().getAllHosts());
			c.setName(cluster.getClusterName());
			return c;
			
		}else{
			throw new RuntimeException("Cluster not defined");
		}		
	}
	

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		if(!session.isClosed()){
			session.close();
		}
		
		if(!cluster.isClosed()){
			cluster.close();
		}
	}
	
}
