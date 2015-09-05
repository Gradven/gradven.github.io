package com.gradven.redisclient;

import java.util.ArrayList;
import java.util.Iterator;
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
            
            List<String> scanRetList = ret.getResult();
            
            int retListSize = retList.size();            
            int scanRetListSize = scanRetList.size();
            
            int tmpNumber = retListSize + scanRetListSize - count;
            
            if (tmpNumber >= 0)
            {
            	int addNumber = scanRetListSize - tmpNumber;
            	List<String> tmpList = new ArrayList<String>();
            	
            	for (int i = 0; i < addNumber; i++)
            	{
            		tmpList.add(scanRetList.get(i));
            	}
            	
            	retList.addAll(tmpList);
            	
            	break;
            			
            }
            else
            {
            	retList.addAll(scanRetList);
            }
            
           
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
		
		rc.returnResouce(jedis);
				
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
		
		rc.returnResouce(jedis);
		
		return ret;
		
	}
	
	public static List<String> queryValue(String key, String redisId)
	{
		return queryValue(key, redisId, 0);
	}
	
	public static List<String> queryValue(String key, String redisId, int database)
	{
		List<String> list = null;
		String retStr = "";
		
		if (key == null || key.equals(""))
		{
			retStr = "The key is null or empty !!";
			
			list = new ArrayList<String>();
			list.add(retStr);

			return list;
		}
		
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(database);
		
		String keyType = jedis.type(key);
		
		//System.out.println("=============:" + keyType);
		
		if (keyType.equals("string"))
		{
			retStr = jedis.get(key);
			
			if (retStr == null || retStr.equals(""))
			{
				retStr = "The value is null !!";				
			}
			
			list = new ArrayList<String>();
			list.add(retStr);
		}
		else if (keyType.equals("list"))
		{
			list = jedis.lrange(key, 0, -1);
			
		}
		else if (keyType.equals("set"))
		{
			Set<String> setValues = jedis.smembers(key);
			
			list = new ArrayList<String>(setValues);
		}
		else if ( keyType.equals("zset"))
		{
			Set<String> setValues = jedis.zrange(key, 0, -1);
			
			list = new ArrayList<String>(setValues);
		}
		
		rc.returnResouce(jedis);
		
		return list;
		
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
		
		rc.returnResouce(jedis);
		
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
		
		rc.returnResouce(jedis);
		
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
		
		
		rc.returnResouce(jedis);
		
		return ret;	
		
	}
	
	/**
	 * sort set length
	 * @param key
	 * @param redisId
	 * @param database
	 * @return
	 */
	public static long zcard(String key, String redisId, int database)
	{
		if (key == null || key.equals(""))
		{
			return 0;
		}
		
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(database);
		
		long ret = jedis.zcard(key);
		
		
		rc.returnResouce(jedis);
		
		return ret;	
		
	}
	
	public static String setString(String key,String value, String redisId, int database)
	{
		if (key == null || key.equals(""))
		{
			return null;
		}
		
		RedisConnection rc = RedisConnFactory.getRedisConn(redisId);
		
		Jedis jedis = rc.getRedisConn();
		jedis.select(database);
		
		String ret = jedis.set(key, value);		
		
		rc.returnResouce(jedis);
		
		return ret;	
		
	}
	
	/**
	 * test redis server is alive.
	 * @param redisid
	 * @return
	 */
	public static boolean isConnected(String redisid)
	{
		RedisConnection rc = null;
		Jedis jedis = null;
		try
		{
			rc = RedisConnFactory.getRedisConn(redisid);
			
			jedis = rc.getRedisConn();
			
			jedis.set("test_redis_server_is_connected", "1");
			jedis.del("test_redis_server_is_connected");
		}
		catch (Exception e) 
		{		
			logger.error("redis connected is error:");
			e.printStackTrace();
			return false;
		}
		finally
		{
			rc.returnResouce(jedis);
		}
	
		return true;
	}
	
	
	

}
