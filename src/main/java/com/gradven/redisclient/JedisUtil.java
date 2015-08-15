package com.gradven.redisclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * 
 * @author GradvenLiu
 * @create date 2015年8月1日 下午2:52:06
 */
public class JedisUtil {
	
	private static Logger logger = Logger.getLogger(JedisUtil.class);  
	
	public static List<String> scanRedis(String pattern, int count, String redisId)
	{
		return scanRedis(pattern, count, redisId, 0);
	}
	
	/**
	 * scan redis
	 * @return
	 */
	public static List<String> scanRedis(String pattern, int count, String redisId, int database)
	{
        List<String> retList = new ArrayList<String>();
        
        RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
        
        Jedis jedis = rc.getRedisConn();
        jedis.select(database);
        
        
        String scanRet = "0";
        
        do {
        	
        	ScanParams scanParams = new ScanParams();
        	
        	scanParams.count(count);  
        	scanParams.match(pattern);
        	
            ScanResult<String> ret = jedis.scan(scanRet, scanParams);
            
            scanRet = ret.getStringCursor();
            
            retList.addAll(ret.getResult());
            
            break;
        } while (!scanRet.equals("0"));
            
        rc.returnResouce(jedis);
        
		return retList;
		
	}
	
	public static Set<String> queryKeys(String pattern, String redisId, int databse)
	{
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(databse);
		Set<String> ret = jedis.keys(pattern);
		
		return ret;
		
	}
	
	public static String queryValue(String key, String redisId)
	{
		return queryValue(key, redisId, 0);
	}
	
	public static String queryValue(String key, String redisId, int database)
	{
		if (key == null || key.equals(""))
		{
			return "The key is null or ''!!";
		}
		
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(database);
		
		String ret = jedis.get(key);
		
		
		if (ret == null || ret.equals(""))
		{
			return "The value is null!!";
		}
		
		return ret;
		
	} 
	
	/**
	 * test redis server is alive.
	 * @param redisid
	 * @return
	 */
	public static boolean isConnected(String redisid)
	{
		try
		{
			RedisConnection rc = RedisConnFactory.getRedisConn(redisid);
			
			Jedis jedis = rc.getRedisConn();
			
			jedis.set("test_redis_server_is_connected", "1");
			jedis.del("test_redis_server_is_connected");
		}
		catch (Exception e) 
		{		
			logger.error("redis connected is error:");
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	
	

}
