package com.gradven.redisclient.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.gradven.redisclient.JedisUtil;
import com.gradven.redisclient.RedisServerManager;
import com.gradven.redisclient.redisinfo.RedisKeyInfo;
import com.gradven.redisclient.redisinfo.RedisServer;

public class RedisServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3649539839025454557L;

	/**
	 * Constructor of the object.
	 */
	public RedisServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

           this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String type = request.getParameter("type");
		String redisdb = request.getParameter("redisdb");
		
		int iRedisdb = 0;
		if (redisdb == null || redisdb.equals(""))
		{
			iRedisdb = 0;
		}
		else
		{
			iRedisdb = Integer.parseInt(redisdb);
		}
		
		HttpSession session = request.getSession();
		
		if (type == null || type.equals("undefined"))
		{
			//just query redis server information
			this.getRedisServerById(request, response);
			
			//set redis id into session
			
			session.setAttribute("redisId", request.getParameter("id"));
		}
		else if(type.equals("1"))
		{
			//test redis server is connected	
			this.testRedisIsConnected(request, response);
			
		}
		else if (type.equals("2"))	
		{
			//redis query value by key
			String redisId = (String) session.getAttribute("redisId");
			String querykey = request.getParameter("querykey");
			
			String ret = "";
			
			List<String> retList = JedisUtil.queryValue(querykey, redisId, iRedisdb);
			
			ret = JSON.toJSONString(retList, true);
			
			this.printWriteOut(ret, response);
			
		}
		else if (type.equals("3"))
		{
			String keyInfoJson = "";
			
			//query key type by key string
			String redisId = (String) session.getAttribute("redisId");
			String querykey = request.getParameter("querykey");
			String countStr = request.getParameter("count");
			
			int count = 0;
			if (countStr == null || countStr.equals("") || countStr.equals("undefined"))
			{
				count = 10;
			}
			else
			{
				count = Integer.parseInt(countStr);
			}
				
			
			keyInfoJson = JSON.toJSONString(this.addKeyInfoList(querykey, count, redisId, iRedisdb));
			
			response.setContentType("text/json");
			this.printWriteOut(keyInfoJson, response);
			
		}

	}
	
	
	
	
	
	
	/**
	 * get Key information List
	 * @param queryKey
	 * @param redisId
	 * @param iRedisdb
	 * @param queryType
	 * @return
	 */
	private List<RedisKeyInfo> addKeyInfoList(String queryKey,int count, String  redisId, int iRedisdb)
	{
		
		List<RedisKeyInfo> keyInfoList = new ArrayList<RedisKeyInfo>();
		
		List<String> keyList = new ArrayList<String>();
		
		//query keys
		keyList = JedisUtil.scanRedis(queryKey, count, redisId, iRedisdb);
			
		
		for (String str : keyList) {  
			String keyType = JedisUtil.queryKeyType(str, redisId, iRedisdb);
			
			long keySize = 1;
			
			if (keyType.equals("string"))
			{
				keySize = 1;
			}
			else if (keyType.equals("list"))					
			{
				keySize = JedisUtil.llen(str, redisId, iRedisdb);
					
			}
			else if (keyType.equals("hash"))
			{
				keySize = JedisUtil.hlen(str, redisId, iRedisdb);
			}
			else if (keyType.equals("set"))
			{
				keySize = JedisUtil.scard(str, redisId, iRedisdb);
			}
			else if( keyType.equals("zset"))
			{
				keySize = JedisUtil.zcard(str, redisId, iRedisdb);
			}
			 
			
			RedisKeyInfo keyInfo  =  new RedisKeyInfo();
			keyInfo.setKey(str);
			keyInfo.setKeyType(keyType);
			keyInfo.setKeySize(keySize);
			
			keyInfoList.add(keyInfo);
		}
		
		return keyInfoList;
		
	}
	
	/**
	 * test Redis Is Connected
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void testRedisIsConnected(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setContentType("text/json");
		
		String id = request.getParameter("id");
		
		if (id == null || id.equals("undefined") || id.trim().equals(""))
		{
			return;
		}
		
		RedisServer rs = RedisServerManager.getRedisServerById(id);
		
		String retString = "";
		if (JedisUtil.isConnected(id))
		{
			retString = JSON.toJSONString(rs);
		}
		else
		{
			retString = "{\"host\":\""+rs.getHost()+"\"," +"\"port\":"+ rs.getPort() +", \"code\": \"connected is error!\"}";
		}
		
		this.printWriteOut(retString, response);
		
	}
	
	private void getRedisServerById(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setContentType("text/json");
		
		String id = request.getParameter("id");
		
		if (id == null || id.equals("undefened") || id.trim().equals(""))
		{
			return;
		}
		
		RedisServer rs = RedisServerManager.getRedisServerById(id);
		
		String redisJson = "";
		
		if (rs != null)
		{
			redisJson = JSON.toJSONString(rs);
			
		}
		else
		{
			redisJson = "null redis server!";
		}
		
		this.printWriteOut(redisJson, response);

		
	}
	
	private void printWriteOut(String str, HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();

		out.println(str);
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
