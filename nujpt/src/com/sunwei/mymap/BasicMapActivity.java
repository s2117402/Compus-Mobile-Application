package com.sunwei.mymap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;

public class BasicMapActivity extends Activity {
	private MapView mapView;//地图控件
    private AMap aMap;
    private TextView tv;//用textview做跑马灯
    static String xianshi;//前面预载入已经下好的跑马灯内容
	String NowTime;
	String NowMsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basicmap_activity);
        FirstRun();         //检测是否第一次运行，以生成time。ini
		mapView = (MapView) findViewById(R.id.map);
		tv=(TextView)findViewById(R.id.tv);
	    mapView.onCreate(savedInstanceState);// 必须要写
	    aMap = mapView.getMap();
	    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.11384,118.930821), 16));
	    tv.setText(xianshi);         //跑马灯内容显示
        final Handler myhandler=new Handler(){         //handler设置
     	   public void handleMessage(Message msg){     //接收到子线程发的消息后的处理措施
     		   if(msg.what==0x123){
     			   MessAge(NowMsg);                    //弹框提示信息   		        
     		   }
     	   }
        };
        //用timer更新，用timer相当于开了一新线程，所以用来更新UI必须用handler
   	 Timer timer=new Timer();
   	 timer.schedule(new TimerTask() {       //TimerTask相当于开启新新线程，所以通过handler的发message和主线程联系来更新UI
 						@Override
 			public void run() {
 				if(hasNew()){           //HasNew（）检测有网页上有无新通知，返回布尔值，如果有则把新日期写入time。ini然后给UI现场发message通知弹框显示
 					write();
 					myhandler.sendEmptyMessage(0x123);
 				}
 				
 			}
 		},0,2000);	
		
	}    

	public void news(View v){        //新闻activity显示
		Intent intent=new Intent();
		intent.setClass(BasicMapActivity.this,NewsActivity.class);
		startActivity(intent);
	}
    public void note(View v){       //通知公告activity显示
		Intent intent=new Intent();
		intent.setClass(BasicMapActivity.this,NoteActivity.class);
		startActivity(intent);
    	
    }
    public void weather(View v){   //天气activity显示
		Intent intent=new Intent();
		intent.setClass(BasicMapActivity.this,WeatherActivity.class);
		startActivity(intent);
    }
    public void login(View v){
    	Intent intent=new Intent();
		intent.setClass(BasicMapActivity.this,LoginActivity.class);
		startActivity(intent);
    }


	public boolean hasNew(){
		String oldtime;
		Date date;//日期类实例主要对比时间进行消息新旧判断
		Date olddate;//同上
		//解决android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
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
		try{ 
			// 创建字符输入流
			URL url=new URL("http://blog.tianya.cn/blog-6936920-1.shtml");
			InputStream instream=url.openStream();
		    	// 用于保存实际读取的字节数

            
 			byte[] buffer=new byte[8000];
 			for(int i=0;i<8000;i++){
             	buffer[i]=(byte)instream.read();
             }
           //存储读取的网页信息
 	       	String txt=new String(buffer,"UTF-8");
 	       	String[] msg=new String[5];
 	        msg=txt.split("(\"title\":\")|(\",\"url\")|(\"time\":\")|(\",\"head\")", 5);
            NowMsg=msg[1];
 	        NowTime=msg[3];
 	       instream.close();
 	 	   SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm"); 
 	 	   FileInputStream fin=new FileInputStream("/mnt/sdcard/time.ini");
 	       Properties props=new Properties(); 
 	       props.load(fin);  
 	       oldtime=props.getProperty("Time").toString();
 	       date = df.parse(NowTime);
 	       olddate=df.parse(oldtime);
 	       fin.close();  
 	       if((!olddate.equals(date))&&olddate.before(date)){
	         return true;

 	          }
 	       return false;
		}
		catch(Exception e){
			return false;
		}	
	}
	public void write(){
	    try{       	  
	    	FileWriter fr = new FileWriter("/mnt/sdcard/time.ini"); 
		    fr.write("Time="+NowTime);
		    fr.flush();
		    fr.close();
	    }
	    catch(Exception e){
	    	//出错设置
	    	e.printStackTrace();
	  
	    }
		

		   
	   }
	   public void FirstRun(){//运行后会判断是否第一次运行并执行相关动作
		   SharedPreferences preferences = getSharedPreferences("count",0);
	       int count = preferences.getInt("count", 0);
	       //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
	       if (count == 0) {//如果首次运行或建立time文件并在其中写入当下时间
	           try{
	               SimpleDateFormat bartDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm"); 
	               Date date = new Date(); 
	               File filename = new File("/mnt/sdcard/time.ini");
	               RandomAccessFile mm = new RandomAccessFile(filename,"rw");
	               mm.writeBytes("Time="+bartDateFormat.format(date));
	               mm.close();
	               Editor editor = preferences.edit();
	               //存入数据
	               editor.putInt("count",++count);
	               //提交修改
	               editor.commit();
	               }
	            catch(Exception e){
	               	e.printStackTrace();
	                              }
	                        }  

	                           }
	     
	public void MessAge(String Str){
		 AlertDialog.Builder builder=new AlertDialog.Builder(BasicMapActivity.this).setTitle("你有一条通知：")
	     		.setMessage("新通知:"+Str+"\n"+"           "+NowTime);
	     builder.setPositiveButton("已阅", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create().show();
	}
}
