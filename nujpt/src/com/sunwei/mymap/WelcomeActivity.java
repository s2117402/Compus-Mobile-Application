package com.sunwei.mymap;

import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;

import com.sunwei.mymap.BasicMapActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {//预载入界面
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		String xx=new String("南邮精神：信达天下 自强不息           ");
		//解决android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  //不在线程中下载信息必须加此代码
        .detectDiskReads()  
        .detectDiskWrites()  
        .detectNetwork()  
        .penaltyLog()  
        .build());   
StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects()  
        .detectLeakedClosableObjects()  
        .penaltyLog()  
        .penaltyDeath()  
        .build());  
//查询天气相关
		try{
			// 创建字符输入流
			URL url=new URL("http://php.weather.sina.com.cn/xml.php?city=%C4%CF%BE%A9&password=DJOYnieT8234jlsK&day=0");
			URL url2=new URL("http://m.njupt.edu.cn/");
			InputStream instream=url.openStream();
			InputStream instream2=url2.openStream();
		    	// 用于保存实际读取的字节数
 
 			byte[] buffer=new byte[1800];
 			byte[] buffer2=new byte[1800];
 			for(int i=0;i<1800;i++){
             	buffer[i]=(byte)instream.read();
             	buffer2[i]=(byte)instream2.read();
             }
         
       	String weather=new String(buffer,"UTF-8");
       	String news=new String(buffer2,"UTF-8");
        String i[]=new String[3];
        i=weather.split("(s1>)|(</status1)", 3);
        String tianqi=new String("校园天气："+i[1]+"     ");
        String[] j=new String[4];
        j=weather.split("(<temperature1>)|(</temperature1>\\n<temperature2>)|(</temperature2>)", 4);
        String qiwen=new String("温度范围："+j[2]+"-"+j[1]+"℃"+"     ");
        String k[]=new String[3];
        k=weather.split("(chy_shuoming>)|(</chy_shuoming>)",3);
        String chuanyi=new String("穿衣提示:"+k[1]+"     ");
        String l[]=new String[5];
        l=news.split("(<p class=\"toutiao_time\">)|(</p>)|(<p class=\"toutiao_t\">)", 5);
        String toutiao="校园头条："+l[3]+"  "+l[1]+"     ";
        BasicMapActivity.xianshi=new String(xx+toutiao+tianqi+qiwen+chuanyi); 
	
				

			instream.close();
		}
		catch(Exception e){
	    BasicMapActivity.xianshi=xx+"网络错误      ";
			
		}
         Timer timer=new Timer();
         timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				 Intent intent=new Intent();
				 intent.setClass(WelcomeActivity.this, BasicMapActivity.class);
				 startActivity(intent);
				 WelcomeActivity.this.finish();

				
			}
		}, 1000);

	}
}
