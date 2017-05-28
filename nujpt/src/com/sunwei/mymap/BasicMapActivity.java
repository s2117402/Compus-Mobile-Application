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
	private MapView mapView;//��ͼ�ؼ�
    private AMap aMap;
    private TextView tv;//��textview�������
    static String xianshi;//ǰ��Ԥ�����Ѿ��ºõ����������
	String NowTime;
	String NowMsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basicmap_activity);
        FirstRun();         //����Ƿ��һ�����У�������time��ini
		mapView = (MapView) findViewById(R.id.map);
		tv=(TextView)findViewById(R.id.tv);
	    mapView.onCreate(savedInstanceState);// ����Ҫд
	    aMap = mapView.getMap();
	    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.11384,118.930821), 16));
	    tv.setText(xianshi);         //�����������ʾ
        final Handler myhandler=new Handler(){         //handler����
     	   public void handleMessage(Message msg){     //���յ����̷߳�����Ϣ��Ĵ����ʩ
     		   if(msg.what==0x123){
     			   MessAge(NowMsg);                    //������ʾ��Ϣ   		        
     		   }
     	   }
        };
        //��timer���£���timer�൱�ڿ���һ���̣߳�������������UI������handler
   	 Timer timer=new Timer();
   	 timer.schedule(new TimerTask() {       //TimerTask�൱�ڿ��������̣߳�����ͨ��handler�ķ�message�����߳���ϵ������UI
 						@Override
 			public void run() {
 				if(hasNew()){           //HasNew�����������ҳ��������֪ͨ�����ز���ֵ����������������д��time��iniȻ���UI�ֳ���message֪ͨ������ʾ
 					write();
 					myhandler.sendEmptyMessage(0x123);
 				}
 				
 			}
 		},0,2000);	
		
	}    

	public void news(View v){        //����activity��ʾ
		Intent intent=new Intent();
		intent.setClass(BasicMapActivity.this,NewsActivity.class);
		startActivity(intent);
	}
    public void note(View v){       //֪ͨ����activity��ʾ
		Intent intent=new Intent();
		intent.setClass(BasicMapActivity.this,NoteActivity.class);
		startActivity(intent);
    	
    }
    public void weather(View v){   //����activity��ʾ
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
		Date date;//������ʵ����Ҫ�Ա�ʱ�������Ϣ�¾��ж�
		Date olddate;//ͬ��
		//���android.os.NetworkOnMainThreadException
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
			// �����ַ�������
			URL url=new URL("http://blog.tianya.cn/blog-6936920-1.shtml");
			InputStream instream=url.openStream();
		    	// ���ڱ���ʵ�ʶ�ȡ���ֽ���

            
 			byte[] buffer=new byte[8000];
 			for(int i=0;i<8000;i++){
             	buffer[i]=(byte)instream.read();
             }
           //�洢��ȡ����ҳ��Ϣ
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
	    	//��������
	    	e.printStackTrace();
	  
	    }
		

		   
	   }
	   public void FirstRun(){//���к���ж��Ƿ��һ�����в�ִ����ض���
		   SharedPreferences preferences = getSharedPreferences("count",0);
	       int count = preferences.getInt("count", 0);
	       //�жϳ�����ڼ������У�����ǵ�һ����������ת������ҳ��
	       if (count == 0) {//����״����л���time�ļ���������д�뵱��ʱ��
	           try{
	               SimpleDateFormat bartDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm"); 
	               Date date = new Date(); 
	               File filename = new File("/mnt/sdcard/time.ini");
	               RandomAccessFile mm = new RandomAccessFile(filename,"rw");
	               mm.writeBytes("Time="+bartDateFormat.format(date));
	               mm.close();
	               Editor editor = preferences.edit();
	               //��������
	               editor.putInt("count",++count);
	               //�ύ�޸�
	               editor.commit();
	               }
	            catch(Exception e){
	               	e.printStackTrace();
	                              }
	                        }  

	                           }
	     
	public void MessAge(String Str){
		 AlertDialog.Builder builder=new AlertDialog.Builder(BasicMapActivity.this).setTitle("����һ��֪ͨ��")
	     		.setMessage("��֪ͨ:"+Str+"\n"+"           "+NowTime);
	     builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create().show();
	}
}
