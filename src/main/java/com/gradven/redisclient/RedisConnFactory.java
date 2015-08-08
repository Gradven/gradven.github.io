package com.gradven.redisclient;

import java.util.ArrayList;
import java.util.List;

import com.gradven.redisclient.redisinfo.RedisServer;

public class RedisConnFactory {
	
	private static List<RedisConnection> connList = new ArrayList<RedisConnection>();
	
	public static RedisConnection getRedisConn(String redisId)
	{
		for (RedisConnection rc : connList)
		{
			if (rc.getRedisServer().getId().equals(redisId))
			{
				return rc;
			}
		}
		
		RedisServer redisServer = RedisServerManager.getRedisServerById(redisId);
		
		RedisConnection rc = new RedisConnection(redisServer);
		
		connList.add(rc);
				
		return rc;
	}

}
