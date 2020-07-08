package com.example.thunderclass;

import android.content.Intent;
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

public class Release extends AppCompatActivity {

    Button release;
    EditText title;
    EditText content;
    String titleString;
    String contentString;
    String time;
    String course_code;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        release = findViewById(R.id.discuss_send);
        title = findViewById(R.id.release_title);
        content = findViewById(R.id.release_content);
        Intent intent = getIntent();
        course_code = intent.getStringExtra("course_code");
        System.out.println(course_code);

        release.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                titleString = title.getText().toString();
                title.setText("");
                contentString = content.getText().toString();
                content.setText("");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                time = df.format(new Date());

                System.out.println(course_code);
                releasepost(titleString,time,contentString,course_code);
            }
        });


    }

    public void releasepost(String title,String time,String content,String id) {
        String url = "http://10.0.2.2:8000/api/add_discuss/";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("discuss_title", title);
            jsonBody.put("discuss_release_time", time);
            jsonBody.put("discuss_content", content);
            jsonBody.put("course_code", id);

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