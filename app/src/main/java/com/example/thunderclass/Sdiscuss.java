package com.example.thunderclass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Sdiscuss extends AppCompatActivity {

    String course_code;
    private List<DiscussArray> discussArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdiscuss);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent tc2 = getIntent();
        course_code = tc2.getStringExtra("course_code");

        final DiscussAdapter discussAdapter = new DiscussAdapter(this, R.layout.discuss_item, discussArray);
        final ListView listView = (ListView) findViewById(R.id.sdiscusslist); //将适配器导入Listview

        postDiscuss(course_code, new Tdiscuss.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result)  {
                try {
                    int discuss_id;
                    String discuss_title;
                    String discuss_content;
                    String discuss_release_time;

                    JSONObject response = new JSONObject(result);
                    JSONArray discuss_list = response.getJSONArray("data");

                    for(int i=0;i<discuss_list.length();i++){
                        JSONObject discuss = (JSONObject) discuss_list.get(i);
                        discuss_id = discuss.getInt("discuss_id");
                        discuss_title = discuss.getString("discuss_title");
                        discuss_content = discuss.getString("discuss_content");
                        discuss_release_time = discuss.getString("discuss_release_time");
                        discussArray.add(new DiscussArray(discuss_id,discuss_title,discuss_content,discuss_release_time.substring(0,10)));//显示信息
                    }
                }
                catch ( JSONException e) {
                    e.printStackTrace();
                }
                listView.setAdapter(discussAdapter);//设置适配器

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                DiscussArray item =(DiscussArray) adapterView.getItemAtPosition(pos);
                Intent tcomment = new Intent();
                tcomment.putExtra("id" ,item.getId());
                tcomment.putExtra("content" ,item.getContent());
                tcomment.putExtra("title" ,item.getTitle());
                tcomment.putExtra("discussTime" ,item.getDiscussTime());
                tcomment.setClass(Sdiscuss.this,Tcomment.class);
                startActivity(tcomment);

            }
        });
    }

    private void postDiscuss(String course_code, final Tdiscuss.VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/api/get_discuss_list/";
        System.out.println(url);
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("course_code", course_code);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        callback.onSuccessResponse(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("onErrorResponse", volleyError.toString());
                    Toast.makeText(getApplicationContext(), "Error: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface VolleyCallback {
        void onSuccessResponse(String result) throws JSONException;
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}