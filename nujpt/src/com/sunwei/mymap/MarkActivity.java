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
		Document cjdoc = Jsoup.parse(cj);//��jsoup��ɹ����ҳ����Ҫ����Ϣ
		String rowRegex = "div.main_box div.mid_box span.formbox table#Datagrid1.datelist tbody tr";//���˵�Ԫ�عؼ���
		Elements rowElements = cjdoc.select(rowRegex);//ѡ������ɸѡ����ɹ��Ԫ�ط���elements��
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();//����װɸ������Ϣ
		for (int i = 0; i < rowElements.size(); i++){
			Elements elements = rowElements.get(i).select("td");//����td�ؼ�����ѡ��Ԫ�ص�һ��������Ԫ��
		    Map<String, Object> map = new HashMap<String, Object>();
		    map.put("k_xuenian", elements.get(0).text()); 
		    map.put("k_xueqi", elements.get(1).text()); 
		    map.put("k_kcname", elements.get(3).text());
		    map.put("k_xuefen", elements.get(6).text());
		    map.put("k_jidian", elements.get(7).text());
		    map.put("k_chengji", elements.get(8).text());
		    list.add(map);
		}
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.k_kccx, new String[] {"k_kcname","k_xuefen","k_jidian","k_chengji"},new int[]{R.id.k_kcname,R.id.k_xuefen,R.id.k_jidian,R.id.k_chengji});//k_kccx������listview�Ĳ��֡��Ĳ������Լ����壬�ں�R.layout.k_kccx, new String[] {"k_kcname","k_xuefen","k_jidian","k_chengji"},new int[]{R.id.k_kcname,R.id.k_xuefen,R.id.k_jidian,R.id.k_chengji����textview��װ����
       lv.setAdapter(adapter);
	}
}
