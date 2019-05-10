package com.example.admin.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProvinceActivity extends AppCompatActivity {


    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";

    private List<String> data2=new ArrayList();
    private String currentlevel= PROVINCE;
    private int cityId=0;
    private int provinceId=0;
    private List<Integer> pids = new ArrayList<>();
    private  List<String> data=new ArrayList<>();
    private List<String> weatherIds=new ArrayList<>();

    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        this.textView = (TextView)findViewById(R.id.textView);
        this.listview=(ListView)findViewById(R.id.listview);

        final ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listview.setAdapter(adapter);
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("点击了哪一个",""+position+":"+ProvinceActivity.this.pids.get(position)+":"+ProvinceActivity.this.data.get(position));
                if (currentlevel==PROVINCE){
                    currentlevel=CITY;
                    provinceId=ProvinceActivity.this.pids.get(position);
                }
                else if(currentlevel==CITY){
                    currentlevel=COUNTRY;
                    cityId=ProvinceActivity.this.pids.get(position);
                }
                else if (currentlevel==COUNTRY){
                    Intent intent=new Intent(ProvinceActivity.this,WeatherActivity.class);
                    intent.putExtra("weatherid",ProvinceActivity.this.weatherIds.get(position));
                    startActivity(intent);
            }
//                provinceId=ProvinceActivity.this.pids.get(position);
//                currentlevel = CITY;
//                getData(adapter);
//                provinceId=ProvinceActivity.this.pids.get(position);
//                currentlevel = COUNTRY;
                getData(adapter);
            }
        });

        getData(adapter);
    }

    private void getData(final ArrayAdapter<String> adapter){
        String weatherUrl = currentlevel== PROVINCE ?"http://guolin.tech/api/china":(currentlevel==CITY?"http://guolin.tech/api/china/"+provinceId:"http://guolin.tech/api/china/"+provinceId+"/"+cityId);
        HttpUtil.sendOkHttpRequest(weatherUrl,new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException{
                final String responseText = response.body().string();
                 parseJSONWithJSONObject(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void  parseJSONWithJSONObject(String responseText) {
        JSONArray jsonArray= null;
        this.data.clear();
        this.pids.clear();
        try {
            jsonArray = new JSONArray(responseText);
            String[] result=new String[jsonArray.length()];
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=null;
                jsonObject = jsonArray.getJSONObject(i);
                this.data.add(jsonObject.getString("name"));
                this.pids.add(jsonObject.getInt("id"));
                if (jsonObject.has("weather_id")){
                    this.weatherIds.add(jsonObject.getString("weather_id"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
