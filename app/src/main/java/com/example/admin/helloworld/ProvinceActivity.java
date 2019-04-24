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


    private List<String> data2=new ArrayList();
    private int[] pids = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private  String[] data={"北京","浙江","安徽","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
    private TextView textView;
    private Button button;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.textView = (TextView)findViewById(R.id.textView);
        this.listview=(ListView)findViewById(R.id.listview);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listview.setAdapter(adapter);
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("点击了哪一个",""+position+":"+ProvinceActivity.this.pids[position]+":"+ProvinceActivity.this.data[position]);
                Intent intent=new Intent(ProvinceActivity.this,CityActivity.class);
                intent.putExtra("pid",ProvinceActivity.this.pids[position]);
                startActivity(intent);
            }
        });

        this.button=(Button)findViewById(R.id.button);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProvinceActivity.this,CityActivity.class));
            }
        });
        String weatherUrl = "http://guolin.tech/api/china";
        HttpUtil.sendOkHttpRequest(weatherUrl,new Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException{
                final String responseText = response.body().string();
                 parseJSONWithJSONObject(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(responseText);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private void  parseJSONWithJSONObject(String responseText) {
        JSONArray jsonArray= null;
        try {
            jsonArray = new JSONArray(responseText);
            String[] result=new String[jsonArray.length()];
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=null;
                jsonObject = jsonArray.getJSONObject(i);
                this.data[i]=jsonObject.getString("name");
                this.pids[i]=jsonObject.getInt("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}