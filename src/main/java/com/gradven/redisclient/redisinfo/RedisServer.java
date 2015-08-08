package com.gradven.redisclient.redisinfo;

/**
 * 
 * @author GradvenLiu
 * @create date 2015-7-14
 */
public class RedisServer {
	
	//jsTree config
	private String id = "";
	
    private String text = "";
    
    
    
    //redis pool connection config
    
    private int maxIdle = 100;
    
    private int minIdle = 20;
    
    private int maxWaitMillis = 1000;
    
    public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	private String host = "";
    
    private int port = 6379;
    
    private int timeout = 100;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
