package com.gradven.redisclient;

import java.util.ArrayList;
import java.util.List;

import com.gradven.redisclient.redisinfo.RedisServer;

public class RedisConnFactory {
	
	private static List<RedisConnection> connList = new ArrayList<RedisConnection>();
	
	public static RedisConnection getRedisConn(String redisId)
	{
		//if find RedisConnection, return RedisConnection;
		for (RedisConnection rc : connList)
		{
			String id = rc.getRedisServer().getId();
			if (id.equals(redisId))
			{
				return rc;
			}
		}
		
		//if cant't find RedisConnection, load RedisConnection from config file;
		RedisServer redisServer = RedisServerManager.getRedisServerById(redisId);
		
		RedisConnection rc = new RedisConnection(redisServer);
		
		connList.add(rc);
				
		return rc;
	}

}
