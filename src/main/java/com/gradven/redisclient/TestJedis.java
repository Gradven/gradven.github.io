package com.gradven.redisclient;

import java.util.List;

/**
 * Created by Kevin on 2015/1/30.
 */
public class TestJedis {
	
    public static void main(String[] args) {
    	
    	List<String> list = JedisUtil.scanRedis("age:*", 9, "1");
    	
    	for (String str : list)
    	{
    		System.out.println("str==========:" + str);
    	}
    	
    	
    	String ss = JedisUtil.queryValue("age:liuhangjun", "1");
    	
    	System.out.println("sssss=============:" + ss);
        
    }

}