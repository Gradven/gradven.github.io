package com.gradven.redisclient;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.gradven.redisclient.redisinfo.RedisRoot;
import com.gradven.redisclient.redisinfo.RedisServer;

/**
 * 
 * @author GradvenLiu
 * @create date 2015-7-14
 */
public class RedisServerManager {
	
	//public static final String redisServerFile = "../webapps/gradvenRedis/json/redis_servers.json";
	public static final String redisServerFile = "E:\\Workspaces\\MyEclipse2014\\gradvenRedis\\webapp\\json\\redis_servers.json";
	
	private static List<RedisRoot> list;
	
	/**
	 * get Redis Server config information by id
	 * @param id
	 * @return
	 */
	public static RedisServer getRedisServerById(String id)
	{
		list = new ArrayList<RedisRoot>();
		String jsonString = FileUtil.readFile(redisServerFile);
		
		try {
			list = (List<RedisRoot>) JSON.parseArray(jsonString, Class.forName("com.gradven.redisclient.redisinfo.RedisRoot"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		for (RedisServer rs : list.get(0).getChildren())
		{
			if (rs.getId().equals(id))
			{
				return rs;
			}
		}
		
		return null;
		
	}

}
