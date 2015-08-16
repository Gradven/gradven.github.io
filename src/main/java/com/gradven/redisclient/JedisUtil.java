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
 * @create date 2015��8��1�� ����2:52:06
 */
public class JedisUtil {
	
	private static Logger logger = Logger.getLogger(JedisUtil.class);  
	
	/**
	 * default database
	 * @param pattern
	 * @param count
	 * @param redisId
	 * @return
	 */
	public static List<String> scanRedis(String pattern, int count, String redisId)
	{
		return scanRedis(pattern, count, redisId, 0);
	}
	
	/**
	 * return limit number key by pattern
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
	
	/**
	 * return one type by one key
	 * @param key
	 * @param redisId
	 * @param databse
	 * @return
	 */
	public static String queryKeyType(String key, String redisId, int databse)
	{
		String keyType = "";
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(databse);
		
		keyType = jedis.type(key);
				
		return keyType;
		
	}
	
	/**
	 * can return little data
	 * @param pattern
	 * @param redisId
	 * @param databse
	 * @return
	 */
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
			return "The key is null or empty !!";
		}
		
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(database);
		
		String ret = jedis.get(key);
		
		
		if (ret == null || ret.equals(""))
		{
			return "The value is null !!";
		}
		
		return ret;
		
	} 
	
	/**
	 * hash length
	 * @param key
	 * @param redisId
	 * @param database
	 * @return
	 */
	public static long hlen(String key, String redisId, int database)
	{
		if (key == null || key.equals(""))
		{
			return 0;
		}
		
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(database);
		
		long ret = jedis.hlen(key);
		
		jedis.llen(key);
		
		return ret;	
		
	}
	
	/**
	 * List length
	 * @param key
	 * @param redisId
	 * @param database
	 * @return
	 */
	public static long llen(String key, String redisId, int database)
	{
		if (key == null || key.equals(""))
		{
			return 0;
		}
		
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(database);
		
		long ret = jedis.llen(key);
		
		
		return ret;	
		
	}
	
	/**
	 * set length
	 * @param key
	 * @param redisId
	 * @param database
	 * @return
	 */
	public static long scard(String key, String redisId, int database)
	{
		if (key == null || key.equals(""))
		{
			return 0;
		}
		
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(database);
		
		long ret = jedis.scard(key);
		
		
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
