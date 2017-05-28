package com.sunwei.mymap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;


import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;





import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	ImageView show;   //放验证码
    InputStream is;    //从client中的数据
    static DefaultHttpClient client;//全局变量为了大家都可使用保存共同cookie
    Handler handler=null;//全局的handler用来自发法通知信息并更新UI
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		show=(ImageView)findViewById(R.id.imageView1); 
		handler=new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==0x124){//收到124通知边读验证码并显示
					Bitmap imageBitmap = BitmapFactory.decodeFile("/mnt/sdcard/yzm.gif");
					show.setImageBitmap(imageBitmap);
				}
				if(msg.what==0x123){
					Toast.makeText(LoginActivity.this, "输入或网络错误，请确认后重试！", Toast.LENGTH_LONG).show();
					getcode(null);//注意失败后刷新验证码
				}
				if(msg.what==0x122){
					Toast.makeText(LoginActivity.this, "验证成功，正在获取成绩！", Toast.LENGTH_SHORT).show();
				}
				
			}
		};
       getcode(null);
	}
	//网络获取验证码保存并提示handler
	public void getcode(View v){//从网站get验证码并保存在手机的代码
		client=new DefaultHttpClient();
		Thread t= new Thread(){
        	public void run(){
        		try {
                    HttpGet httpget = new HttpGet("http://42.247.7.170/CheckCode.aspx");
                    httpget.setHeader("Host","42.247.7.170");
                    httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
                    httpget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    httpget.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
                    httpget.setHeader("Accept-Encoding","gzip, deflate");
                    HttpResponse response=client.execute(httpget);
                    if(response.getStatusLine().getStatusCode()==200){
                    	is = response.getEntity().getContent();
                    	OutputStream out = new FileOutputStream(new File("/mnt/sdcard/yzm.gif"));
                    	int read = 0;  
                    	byte[] bytes = new byte[1024];  
                    	                       
                    	while ((read = is.read(bytes)) != -1) {  
                    	    out.write(bytes, 0, read);  
                    	}  
                    	                       
                    	is.close();  
                    	out.flush();  
                    	out.close();  
                    	handler.sendEmptyMessage(0x124);//验证码已下好，提示show更新显示
                    
                    	
                 	   }
                    

                    }catch(Exception e){
                 	   e.printStackTrace();
                 	   }
            	
        	}
        };
        t.start();
	}
	  public void submit(View v){//提交后进行几次get和post返回所需值
		  Toast.makeText(LoginActivity.this, "正在验证，请您耐心等待，谢谢！（请不要重复点击，以免出现错误）", Toast.LENGTH_LONG).show();
		  String checkcode=((EditText)findViewById(R.id.editText3)).getText().toString();
		  String password=((EditText)findViewById(R.id.editText2)).getText().toString();
		  final String number=((EditText)findViewById(R.id.editText1)).getText().toString();
		  final boolean[] ok={false};//布尔变量用来之后post中传递成功与否，用集合是英文显示数据不闭合
			  String url = "http://42.247.7.170/default2.aspx";
				final HttpPost httpPost = new HttpPost(url);
				final HttpGet get = new HttpGet("http://42.247.7.170/xs_main.aspx?xh="+number);			
			   httpPost.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);//次也会有成功后转跳，此设置用来禁止自动转跳
		       httpPost.setHeader("Host","42.247.7.170");//之后都是发送头消息
		       httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
		       httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		       httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		       httpPost.setHeader("Accept-Encoding","gzip, deflate");
		       get.setHeader("Host","42.247.7.170");
		       get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
		       get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		       get.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		       get.setHeader("Accept-Encoding","gzip, deflate");
		       get.setHeader("Referer"," http://42.247.7.170/default2.aspx");	       
		      final List<NameValuePair> params = new ArrayList<NameValuePair>();
			  params.add(new BasicNameValuePair("__VIEWSTATE","dDwyODE2NTM0OTg7Oz7qYRVlUu9NQsHuMh3idTyISTXDHQ=="));//以下为post发送中的消息，夹带帐密等重要信息
		      params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","92719903"));
		      params.add(new BasicNameValuePair("txtUserName",number));
		      params.add(new BasicNameValuePair("TextBox2",password));
		      params.add(new BasicNameValuePair("txtSecretCode",checkcode));
		      params.add(new BasicNameValuePair("RadioButtonList1", "学生"));
		      params.add(new BasicNameValuePair("Button1", ""));
		      params.add(new BasicNameValuePair("lbLanguage", ""));
		      params.add(new BasicNameValuePair("hidPdrs",""));
		      params.add(new BasicNameValuePair("hidsc",""));
		      final List<NameValuePair> paramsgra1 = new ArrayList<NameValuePair>();
		      paramsgra1.add(new BasicNameValuePair("__VIEWSTATE","dDwxODI2NTc3MzMwO3Q8cDxsPHhoOz47bDwxMjAwMjMxNDs+PjtsPGk8MT47PjtsPHQ8O2w8aTwxPjtpPDM+O2k8NT47aTw3PjtpPDk+O2k8MTE+O2k8MTM+O2k8MTY+O2k8MjY+O2k8Mjc+O2k8Mjg+O2k8MzU+O2k8Mzc+O2k8Mzk+O2k8NDE+O2k8NDU+Oz47bDx0PHA8cDxsPFRleHQ7PjtsPOWtpuWPt++8mjEyMDAyMzE0Oz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlp5PlkI3vvJrlrZnljas7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWtpumZou+8mumAmui+vuWtpumZojs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85LiT5Lia77yaOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzpgJrkv6Hlt6XnqIvvvIjpgJrovr7vvIk7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOihjOaUv+ePre+8mjEyMDAyMzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MjAxMjk5MDI7Pj47Pjs7Pjt0PHQ8cDxwPGw8RGF0YVRleHRGaWVsZDtEYXRhVmFsdWVGaWVsZDs+O2w8WE47WE47Pj47Pjt0PGk8MTk+O0A8XGU7MjAxNS0yMDE2OzIwMTQtMjAxNTsyMDEzLTIwMTQ7MjAxMi0yMDEzOzIwMTEtMjAxMjsyMDEwLTIwMTE7MjAwOS0yMDEwOzIwMDgtMjAxMDsyMDA4LTIwMDk7MjAwNy0yMDA4OzIwMDYtMjAwNzsyMDA1LTIwMDY7MjAwNC0yMDA1OzIwMDMtMjAwNDsyMDAyLTIwMDM7MjAwMS0yMDAyOzIwMDAtMjAwMTsxOTk5LTIwMDA7PjtAPFxlOzIwMTUtMjAxNjsyMDE0LTIwMTU7MjAxMy0yMDE0OzIwMTItMjAxMzsyMDExLTIwMTI7MjAxMC0yMDExOzIwMDktMjAxMDsyMDA4LTIwMTA7MjAwOC0yMDA5OzIwMDctMjAwODsyMDA2LTIwMDc7MjAwNS0yMDA2OzIwMDQtMjAwNTsyMDAzLTIwMDQ7MjAwMi0yMDAzOzIwMDEtMjAwMjsyMDAwLTIwMDE7MTk5OS0yMDAwOz4+Oz47Oz47dDxwPDtwPGw8b25jbGljazs+O2w8d2luZG93LnByaW50KClcOzs+Pj47Oz47dDxwPDtwPGw8b25jbGljazs+O2w8d2luZG93LmNsb3NlKClcOzs+Pj47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPHQ+Oz4+Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDw7bDxpPDA+O2k8MT47aTwyPjtpPDQ+Oz47bDx0PDtsPGk8MD47aTwxPjs+O2w8dDw7bDxpPDA+O2k8MT47PjtsPHQ8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PDtsPGk8MD47aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+O3Q8O2w8aTwwPjs+O2w8dDw7bDxpPDA+Oz47bDx0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+O3Q8O2w8aTwwPjtpPDE+Oz47bDx0PDtsPGk8MD47PjtsPHQ8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47Pj47dDw7bDxpPDA+Oz47bDx0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+O3Q8O2w8aTwwPjs+O2w8dDw7bDxpPDA+Oz47bDx0PHA8cDxsPFRleHQ7PjtsPFpKVTs+Pjs+Ozs+Oz4+Oz4+Oz4+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47PlFayQfFPi5VJd9j2SfFnNBDRHXp"));
		      paramsgra1.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","DB0F94E3"));
		      paramsgra1.add(new BasicNameValuePair("ddlXQ",""));
		      paramsgra1.add(new BasicNameValuePair("ddlXN",""));
		      paramsgra1.add(new BasicNameValuePair("Button1","在校学习成绩查询"));
		      new Thread(){//开线程来模拟登录获取数据
			          public void run(){
			        	  try{
			             	 httpPost.setEntity(new UrlEncodedFormEntity(params,"gb2312"));
			             	 HttpResponse response = client.execute(httpPost);
			             	 httpPost.abort();
			             	Log.i("tag","post1excute成功");
			             	int Status = response.getStatusLine().getStatusCode();
			             	if(Status == 302){
			             		Log.i("tag","post1成功");
			             	     try{
				                    	HttpResponse httpResponse =client.execute(get);	                   	
				                    	    if(httpResponse.getStatusLine().getStatusCode() == 200){
				                    	    	Log.i("tag","get成功");
				                    	    	HttpEntity entityname = httpResponse.getEntity();
	                                         	String nametxt=EntityUtils.toString(entityname, HTTP.UTF_8);    //注意UTF_8,之前使用其他编码会乱码           	    	
	                                         	String name[]=new String[3];//此步扣下学生姓名，之后要用
		                                          name=nametxt.split("(&xm=)|(&gnmkdm=)",3);
		                                          String decodename = null;
		                                		  try{decodename=URLEncoder.encode(name[1],"GBK");}catch(Exception e){}
		                                		  String url2="http://42.247.7.170/xscj_gc.aspx?xh="+number+"&xm="+decodename+"&gnmkdm=N121605";	  
		                                          FileWriter fr = new FileWriter("/mnt/sdcard/name.txt");
		                                          final HttpGet kbget = new HttpGet(url2);
		                              			final HttpPost post = new HttpPost(url2);
		                              	       kbget.setHeader("Host","42.247.7.170");
		                              	       kbget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
		                              	       kbget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		                              	       kbget.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		                              	       kbget.setHeader("Referer", "http://42.247.7.170/xs_main.aspx?xh="+number);
		                              	       kbget.setHeader("Accept-Encoding","gzip, deflate");
		                              	       post.setHeader("Host","42.247.7.170");
		                              	       post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
		                              	       post.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		                              	       post.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		                              	       post.setHeader("Accept-Encoding","gzip, deflate");
		                              	       post.setHeader("Referer",url2);
		                  	    	   		       fr.write(name[1]);
		                  	    	   		       fr.flush();
		                  	    	   		       fr.close();
		                  	    	   		      get.abort();
				                    	    	try{
				                    	    		  
				                  	              HttpResponse responsecj1 = client.execute(kbget); 
				                  	              kbget.abort();
				                  	              if(responsecj1.getStatusLine().getStatusCode()==200){  	  
				                  	            	  Log.i("tag","成绩查询get成功");
				                  	            	post.setEntity(new UrlEncodedFormEntity(paramsgra1,"gb2312"));
				                  	            	try{
				                  	            		HttpResponse responsecj2 = client.execute(post);         	            		
							                  	          if(responsecj2.getStatusLine().getStatusCode()==200){
							                  	        	  ok[0]=true;
				                  	            			Log.i("tag","成绩查询post成功");
				                  	            			HttpEntity entitycj2 = responsecj2.getEntity();	 	            	  
				                  	    	               String result=EntityUtils.toString(entitycj2, HTTP.UTF_8); 
				                  	    	               MarkActivity.cj=result;//下一显示成绩窗口变量cj接受信息
				                  	    	             post.abort();
				                  	    	   		      handler.sendEmptyMessage(0x122);//成功提示主程序UI更新显示成功toast
				                  		            	  Log.i("tag","最后一步成功");
				                  		            	Intent intent=new Intent();//成功后会弹出新窗口来显示成绩
				                  		      		intent.setClass(LoginActivity.this,MarkActivity.class);
				                  		      		startActivity(intent);
				                  		      		LoginActivity.this.finish();
				                  	            		}
				                  	            	}catch(Exception e){
				                  	            		Log.i("tag","最后一步失败");
				                  	            	}
				                  	   		    }
				                  	             
				                  	                }catch(Exception e){Log.i("tag","最后一步失败");}
				                    	    	
				                    	    }

				                    }catch(Exception e){Log.i("tag","get错误");}
			             		
			               
			             		
			             	}else{Log.i("tag",""+Status+"post1失败");
			             	}
			             	
			           
			              }catch(Exception e){
			            	  Log.i("tag","post1失败");
			              }
					         if(ok[0]==false){//如果失败ok【01依旧为false，触发handlertoast提示失败
						   		 handler.sendEmptyMessage(0x123);
					  } 
			          }
			         }.start();


	  }	
}
