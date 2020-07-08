package com.example.thunderclass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reply extends AppCompatActivity {

    Button send;
    EditText reply;
    String content;
    String username;
    String time;
    int discuss_id;
    private SharedPreferences mSharePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSharePreferences = getSharedPreferences("user",MODE_PRIVATE);//从本地获取username
        username = mSharePreferences.getString("username","null");

        reply = findViewById(R.id.replycontent);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                time = df.format(new Date());
                content = reply.getText().toString();
                reply.setText("");
                Intent intent = getIntent();
                discuss_id=intent.getIntExtra("discuss_id",discuss_id);
                replypost(username,time,content,discuss_id);
            }
        });
    }
    public void replypost(String username,String time,String content,int id) {
        String url = "http://10.0.2.2:8000/api/add_comment/";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("username", username);
            //jsonBody.put("username", "ttest");//测试
           // jsonBody.put("comment_time", time);时间
            jsonBody.put("comment_content", content);
            jsonBody.put("discuss_id", id);
            System.out.println("id="+id);
            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("onResponse", jsonObject.toString());
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("onErrorResponse", volleyError.toString());
                }
            });
            queue.add(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}