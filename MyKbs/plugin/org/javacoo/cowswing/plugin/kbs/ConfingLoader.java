/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-8-13 上午11:16:20
 * @version 1.0
 */
public class ConfingLoader {
	public  void getResource() throws IOException{     
        //查找指定资源的URL，其中res.txt仍然开始的bin目录下    
//	  URL fileURL=this.getClass().getResource("/Plugin.properties");    
//	  System.out.println(fileURL.getFile()); 
		 InputStream is=this.getClass().getResourceAsStream("/org/javacoo/cowswing/plugin/kbs/config/Plugin.properties");   
	        BufferedReader br=new BufferedReader(new InputStreamReader(is));  
	        String s="";  
	        while((s=br.readLine())!=null)  
	            System.out.println(s);  
	}   
	public static void main(String[] args) throws IOException {   
		ConfingLoader res=new ConfingLoader();   
	  res.getResource();   
	}   
}
