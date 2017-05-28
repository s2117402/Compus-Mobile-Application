package com.sunwei.mymap;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MarkActivity extends Activity {
     static String cj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark);
		ListView lv=(ListView)findViewById(R.id.listView1);
		Document cjdoc = Jsoup.parse(cj);//用jsoup来晒出网页中需要的信息
		String rowRegex = "div.main_box div.mid_box span.formbox table#Datagrid1.datelist tbody tr";//过滤的元素关键字
		Elements rowElements = cjdoc.select(rowRegex);//选用以上筛选条件晒出元素放入elements中
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();//用来装筛出的信息
		for (int i = 0; i < rowElements.size(); i++){
			Elements elements = rowElements.get(i).select("td");//再以td关键字在选出元素第一行来放入元素
		    Map<String, Object> map = new HashMap<String, Object>();
		    map.put("k_xuenian", elements.get(0).text()); 
		    map.put("k_xueqi", elements.get(1).text()); 
		    map.put("k_kcname", elements.get(3).text());
		    map.put("k_xuefen", elements.get(6).text());
		    map.put("k_jidian", elements.get(7).text());
		    map.put("k_chengji", elements.get(8).text());
		    list.add(map);
		}
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.k_kccx, new String[] {"k_kcname","k_xuefen","k_jidian","k_chengji"},new int[]{R.id.k_kcname,R.id.k_xuefen,R.id.k_jidian,R.id.k_chengji});//k_kccx是所用listview的布局。改布局又自己定义，内含R.layout.k_kccx, new String[] {"k_kcname","k_xuefen","k_jidian","k_chengji"},new int[]{R.id.k_kcname,R.id.k_xuefen,R.id.k_jidian,R.id.k_chengji几个textview来装数据
       lv.setAdapter(adapter);
	}
}
