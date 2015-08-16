package com.gradven.redisclient.redisinfo;

public class RedisKeyInfo {
	
	private String key = "";
	
	private String keyType = "";
	
	private long keySize = 1;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public long getKeySize() {
		return keySize;
	}

	public void setKeySize(long keySize) {
		this.keySize = keySize;
	}
	
	

}
