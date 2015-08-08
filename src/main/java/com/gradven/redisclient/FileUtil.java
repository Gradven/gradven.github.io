package com.gradven.redisclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * operate file
 * @author GradvenLiu
 * @create date 2015-7-13
 */
public class FileUtil {
	
	private static Logger logger = Logger.getLogger(FileUtil.class);  
	
	public static void writeFile(String content, String filePath)
	{
		
	}
	
	public static String readFile(String filePath)
	{
		String allcontent = "";
		 try {
             String encoding="UTF-8";
             File file=new File(filePath);
             
             //System.out.println("user.dir:" + System.getProperty("user.dir"));
             
             if(file.isFile() && file.exists())
             { 
                 InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                
                 BufferedReader bufferedReader = new BufferedReader(read);
                
                 
                 String lineContent = "";
                 
                 while((lineContent = bufferedReader.readLine()) != null){
                	 allcontent = allcontent + "\n" + lineContent;
                 }
                 read.close();
            }
            else
             {
            	logger.error("can't find file!!!");
             }
     } catch (Exception e) 
     {
         
    	 logger.error("read file error!!!" + e.toString());
    	 e.printStackTrace();
     }
		return allcontent;
		
	}

}
