package com.gradven.redisclient;

import org.apache.log4j.Logger;

import com.gradven.redisclient.redisinfo.RedisServer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis Connection manager
 * @author GradvenLiu
 * @create date 2015Äê8ÔÂ1ÈÕ
 */
public class RedisConnection {
	
	private static Logger logger = Logger.getLogger(FileUtil.class);  
	
	private JedisPool pool = null;
	
	private RedisServer redisServer = null;
	

	public RedisConnection(RedisServer redisServer)
	{
		this.redisServer = redisServer;
	}
	
	public Jedis getRedisConn()
	{
		if (redisServer == null)
		{
			logger.error("RedisServer is null ,can't get Jedis instance!!!");
			return null;
		}
		
		JedisPoolConfig config = new JedisPoolConfig();
       
		config.setMaxIdle(redisServer.getMaxIdle());
        config.setMinIdle(redisServer.getMinIdle());
        
        config.setMaxWaitMillis(redisServer.getMaxWaitMillis());
        
		pool = new JedisPool(config, redisServer.getHost(), redisServer.getPort());
		
		
		Jedis jedis = pool.getResource();
		
		return jedis;
		
	}
	
	
	public void returnResouce(Jedis jedis)
	{
		//after use,return jedis to the pool
		if (jedis != null)
		{
			pool.returnResource(jedis);
		}
		
	}
	
	public RedisServer getRedisServer() {
		return redisServer;
	}

	public void setRedisServer(RedisServer redisServer) {
		this.redisServer = redisServer;
	}


}
