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
	ImageView show;   //����֤��
    InputStream is;    //��client�е�����
    static DefaultHttpClient client;//ȫ�ֱ���Ϊ�˴�Ҷ���ʹ�ñ��湲ͬcookie
    Handler handler=null;//ȫ�ֵ�handler�����Է���֪ͨ��Ϣ������UI
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		show=(ImageView)findViewById(R.id.imageView1); 
		handler=new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==0x124){//�յ�124֪ͨ�߶���֤�벢��ʾ
					Bitmap imageBitmap = BitmapFactory.decodeFile("/mnt/sdcard/yzm.gif");
					show.setImageBitmap(imageBitmap);
				}
				if(msg.what==0x123){
					Toast.makeText(LoginActivity.this, "��������������ȷ�Ϻ����ԣ�", Toast.LENGTH_LONG).show();
					getcode(null);//ע��ʧ�ܺ�ˢ����֤��
				}
				if(msg.what==0x122){
					Toast.makeText(LoginActivity.this, "��֤�ɹ������ڻ�ȡ�ɼ���", Toast.LENGTH_SHORT).show();
				}
				
			}
		};
       getcode(null);
	}
	//�����ȡ��֤�뱣�沢��ʾhandler
	public void getcode(View v){//����վget��֤�벢�������ֻ��Ĵ���
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
                    	handler.sendEmptyMessage(0x124);//��֤�����ºã���ʾshow������ʾ
                    
                    	
                 	   }
                    

                    }catch(Exception e){
                 	   e.printStackTrace();
                 	   }
            	
        	}
        };
        t.start();
	}
	  public void submit(View v){//�ύ����м���get��post��������ֵ
		  Toast.makeText(LoginActivity.this, "������֤���������ĵȴ���лл�����벻Ҫ�ظ������������ִ���", Toast.LENGTH_LONG).show();
		  String checkcode=((EditText)findViewById(R.id.editText3)).getText().toString();
		  String password=((EditText)findViewById(R.id.editText2)).getText().toString();
		  final String number=((EditText)findViewById(R.id.editText1)).getText().toString();
		  final boolean[] ok={false};//������������֮��post�д��ݳɹ�����ü�����Ӣ����ʾ���ݲ��պ�
			  String url = "http://42.247.7.170/default2.aspx";
				final HttpPost httpPost = new HttpPost(url);
				final HttpGet get = new HttpGet("http://42.247.7.170/xs_main.aspx?xh="+number);			
			   httpPost.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);//��Ҳ���гɹ���ת����������������ֹ�Զ�ת��
		       httpPost.setHeader("Host","42.247.7.170");//֮���Ƿ���ͷ��Ϣ
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
			  params.add(new BasicNameValuePair("__VIEWSTATE","dDwyODE2NTM0OTg7Oz7qYRVlUu9NQsHuMh3idTyISTXDHQ=="));//����Ϊpost�����е���Ϣ���д����ܵ���Ҫ��Ϣ
		      params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","92719903"));
		      params.add(new BasicNameValuePair("txtUserName",number));
		      params.add(new BasicNameValuePair("TextBox2",password));
		      params.add(new BasicNameValuePair("txtSecretCode",checkcode));
		      params.add(new BasicNameValuePair("RadioButtonList1", "ѧ��"));
		      params.add(new BasicNameValuePair("Button1", ""));
		      params.add(new BasicNameValuePair("lbLanguage", ""));
		      params.add(new BasicNameValuePair("hidPdrs",""));
		      params.add(new BasicNameValuePair("hidsc",""));
		      final List<NameValuePair> paramsgra1 = new ArrayList<NameValuePair>();
		      paramsgra1.add(new BasicNameValuePair("__VIEWSTATE","dDwxODI2NTc3MzMwO3Q8cDxsPHhoOz47bDwxMjAwMjMxNDs+PjtsPGk8MT47PjtsPHQ8O2w8aTwxPjtpPDM+O2k8NT47aTw3PjtpPDk+O2k8MTE+O2k8MTM+O2k8MTY+O2k8MjY+O2k8Mjc+O2k8Mjg+O2k8MzU+O2k8Mzc+O2k8Mzk+O2k8NDE+O2k8NDU+Oz47bDx0PHA8cDxsPFRleHQ7PjtsPOWtpuWPt++8mjEyMDAyMzE0Oz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlp5PlkI3vvJrlrZnljas7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWtpumZou+8mumAmui+vuWtpumZojs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85LiT5Lia77yaOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzpgJrkv6Hlt6XnqIvvvIjpgJrovr7vvIk7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOihjOaUv+ePre+8mjEyMDAyMzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MjAxMjk5MDI7Pj47Pjs7Pjt0PHQ8cDxwPGw8RGF0YVRleHRGaWVsZDtEYXRhVmFsdWVGaWVsZDs+O2w8WE47WE47Pj47Pjt0PGk8MTk+O0A8XGU7MjAxNS0yMDE2OzIwMTQtMjAxNTsyMDEzLTIwMTQ7MjAxMi0yMDEzOzIwMTEtMjAxMjsyMDEwLTIwMTE7MjAwOS0yMDEwOzIwMDgtMjAxMDsyMDA4LTIwMDk7MjAwNy0yMDA4OzIwMDYtMjAwNzsyMDA1LTIwMDY7MjAwNC0yMDA1OzIwMDMtMjAwNDsyMDAyLTIwMDM7MjAwMS0yMDAyOzIwMDAtMjAwMTsxOTk5LTIwMDA7PjtAPFxlOzIwMTUtMjAxNjsyMDE0LTIwMTU7MjAxMy0yMDE0OzIwMTItMjAxMzsyMDExLTIwMTI7MjAxMC0yMDExOzIwMDktMjAxMDsyMDA4LTIwMTA7MjAwOC0yMDA5OzIwMDctMjAwODsyMDA2LTIwMDc7MjAwNS0yMDA2OzIwMDQtMjAwNTsyMDAzLTIwMDQ7MjAwMi0yMDAzOzIwMDEtMjAwMjsyMDAwLTIwMDE7MTk5OS0yMDAwOz4+Oz47Oz47dDxwPDtwPGw8b25jbGljazs+O2w8d2luZG93LnByaW50KClcOzs+Pj47Oz47dDxwPDtwPGw8b25jbGljazs+O2w8d2luZG93LmNsb3NlKClcOzs+Pj47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPHQ+Oz4+Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDw7bDxpPDA+O2k8MT47aTwyPjtpPDQ+Oz47bDx0PDtsPGk8MD47aTwxPjs+O2w8dDw7bDxpPDA+O2k8MT47PjtsPHQ8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PDtsPGk8MD47aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+O3Q8O2w8aTwwPjs+O2w8dDw7bDxpPDA+Oz47bDx0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+O3Q8O2w8aTwwPjtpPDE+Oz47bDx0PDtsPGk8MD47PjtsPHQ8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47Pj47dDw7bDxpPDA+Oz47bDx0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+O3Q8O2w8aTwwPjs+O2w8dDw7bDxpPDA+Oz47bDx0PHA8cDxsPFRleHQ7PjtsPFpKVTs+Pjs+Ozs+Oz4+Oz4+Oz4+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47PlFayQfFPi5VJd9j2SfFnNBDRHXp"));
		      paramsgra1.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","DB0F94E3"));
		      paramsgra1.add(new BasicNameValuePair("ddlXQ",""));
		      paramsgra1.add(new BasicNameValuePair("ddlXN",""));
		      paramsgra1.add(new BasicNameValuePair("Button1","��Уѧϰ�ɼ���ѯ"));
		      new Thread(){//���߳���ģ���¼��ȡ����
			          public void run(){
			        	  try{
			             	 httpPost.setEntity(new UrlEncodedFormEntity(params,"gb2312"));
			             	 HttpResponse response = client.execute(httpPost);
			             	 httpPost.abort();
			             	Log.i("tag","post1excute�ɹ�");
			             	int Status = response.getStatusLine().getStatusCode();
			             	if(Status == 302){
			             		Log.i("tag","post1�ɹ�");
			             	     try{
				                    	HttpResponse httpResponse =client.execute(get);	                   	
				                    	    if(httpResponse.getStatusLine().getStatusCode() == 200){
				                    	    	Log.i("tag","get�ɹ�");
				                    	    	HttpEntity entityname = httpResponse.getEntity();
	                                         	String nametxt=EntityUtils.toString(entityname, HTTP.UTF_8);    //ע��UTF_8,֮ǰʹ���������������           	    	
	                                         	String name[]=new String[3];//�˲�����ѧ��������֮��Ҫ��
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
				                  	            	  Log.i("tag","�ɼ���ѯget�ɹ�");
				                  	            	post.setEntity(new UrlEncodedFormEntity(paramsgra1,"gb2312"));
				                  	            	try{
				                  	            		HttpResponse responsecj2 = client.execute(post);         	            		
							                  	          if(responsecj2.getStatusLine().getStatusCode()==200){
							                  	        	  ok[0]=true;
				                  	            			Log.i("tag","�ɼ���ѯpost�ɹ�");
				                  	            			HttpEntity entitycj2 = responsecj2.getEntity();	 	            	  
				                  	    	               String result=EntityUtils.toString(entitycj2, HTTP.UTF_8); 
				                  	    	               MarkActivity.cj=result;//��һ��ʾ�ɼ����ڱ���cj������Ϣ
				                  	    	             post.abort();
				                  	    	   		      handler.sendEmptyMessage(0x122);//�ɹ���ʾ������UI������ʾ�ɹ�toast
				                  		            	  Log.i("tag","���һ���ɹ�");
				                  		            	Intent intent=new Intent();//�ɹ���ᵯ���´�������ʾ�ɼ�
				                  		      		intent.setClass(LoginActivity.this,MarkActivity.class);
				                  		      		startActivity(intent);
				                  		      		LoginActivity.this.finish();
				                  	            		}
				                  	            	}catch(Exception e){
				                  	            		Log.i("tag","���һ��ʧ��");
				                  	            	}
				                  	   		    }
				                  	             
				                  	                }catch(Exception e){Log.i("tag","���һ��ʧ��");}
				                    	    	
				                    	    }

				                    }catch(Exception e){Log.i("tag","get����");}
			             		
			               
			             		
			             	}else{Log.i("tag",""+Status+"post1ʧ��");
			             	}
			             	
			           
			              }catch(Exception e){
			            	  Log.i("tag","post1ʧ��");
			              }
					         if(ok[0]==false){//���ʧ��ok��01����Ϊfalse������handlertoast��ʾʧ��
						   		 handler.sendEmptyMessage(0x123);
					  } 
			          }
			         }.start();


	  }	
}
