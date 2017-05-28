package com.sunwei.mymap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class NoteActivity extends Activity {
	WebView wv2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		wv2=(WebView)findViewById(R.id.wv2);
		wv2.loadUrl("http://m.baidu.com/from=1099a/bd_page_type=1/ssid=0/uid=0/baiduid=D95804620FFB1FB408F50ED28FD63A07/w=0_10_%E5%8D%97%E4%BA%AC%E9%82%AE%E7%94%B5%E5%A4%A7%E5%AD%A6%E9%80%9A%E7%9F%A5%E5%85%AC%E5%91%8A/t=zbios/l=3/tc?ref=www_zbios&pu=sz%401320_480%2Ccuid%40F1CA35D549AD31E4AEEB755294AAEC924718108E3FRGETELGOL%2Ccua%40750_1334_iphone_6.4.1.0_0%2Ccut%40iPhone7%252C2_8.1.2%2Cosname%40baiduboxapp%2Cctv%401%2Ccfrom%401099a%2Ccsrc%40app_box_txt%2Cta%40zbios_1_8.1_6_0.0&lid=14849656099602096442&order=3&vit=osres&tj=www_normal_3_0_10_title&m=8&srd=1&cltj=cloud_title&dict=30&title=%E9%80%9A%E7%9F%A5%E5%85%AC%E5%91%8A&sec=7025&di=0a1f7a2a04fae6c4&bdenc=1&tch=124.0.0.0.0.0&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IEQGG_yBOByaa95izbbrgHhEsRCCqAp8HZpPPsCPQpx9Ywk0b3mRU7wV2mvAxcC56kzm9");
	}
}
