package com.sunwei.mymap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class WeatherActivity extends Activity {
	WebView wv3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		wv3=(WebView)findViewById(R.id.wv3);
		wv3.loadUrl("http://m.weather.com.cn/mweather/101190101.shtml");
	}
}
