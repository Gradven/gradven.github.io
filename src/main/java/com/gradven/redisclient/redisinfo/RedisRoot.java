package com.gradven.redisclient.redisinfo;

import java.util.List;

/**
 * 
 * @author GradvenLiu
 * @create date 2015Äê8ÔÂ1ÈÕ
 */
public class RedisRoot {
	
	private String id = "";
	
    private String text = "";
    
    private List<RedisServer> children  = null;

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

	public List<RedisServer> getChildren() {
		return children;
	}

	public void setChildren(List<RedisServer> children) {
		this.children = children;
	}

}
