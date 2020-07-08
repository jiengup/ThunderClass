package com.example.thunderclass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class Tcomment extends AppCompatActivity {

    Button reply;
    TextView title;
    TextView content;
    TextView time;
    TextView starNumber;
    int discuss_id;

    private List<CommentArray> commentArrays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcomment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reply = findViewById(R.id.reply);
        title = findViewById(R.id.comment_title);
        content = findViewById(R.id.comment_content);
        time = findViewById(R.id.comment_time);

        final Intent discuss = getIntent();
        title.setText(discuss.getStringExtra("title"));
        content.setText(discuss.getStringExtra("content"));
        time.setText(discuss.getStringExtra("discussTime"));
        discuss_id = discuss.getIntExtra("id",discuss_id);


        reply.setOnClickListener(new View.OnClickListener() {//发表评论
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("discuss_id",discuss_id);
                intent.setClass(Tcomment.this,Reply.class);
                startActivity(intent);
            }
        });

        final CommentAdapter commentAdapter = new CommentAdapter(this,R.layout.comment_item,commentArrays);
        final ListView listView = (ListView) findViewById(R.id.tcomment_list);

//接收评论列表

        postComment(discuss_id, new Tdiscuss.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result)  {
                try {
                    int comment_id;
                    String comment_title;
                    String comment_username;
                    String comment_nickname;
                    String comment_content;
                    String comment_time;
                    String portrait_url;
                    int star_number;

                    JSONObject response = new JSONObject(result);
                    JSONArray discuss_list = response.getJSONArray("data");//可能

                    for(int i=0;i<discuss_list.length();i++){
                        JSONObject discuss = (JSONObject) discuss_list.get(i);
                        comment_id = discuss.getInt("comment_id");
                        star_number = discuss.getInt("comment_star_number");
                        comment_content = discuss.getString("comment_content");
                        comment_username = discuss.getString("comment_username");
                        comment_nickname = discuss.getString("comment_nickname");
                        comment_time = discuss.getString("comment_time");
                        portrait_url = "http://10.0.2.2:8000" + discuss.getString("portrait_url");
                        commentArrays.add(new CommentArray(comment_id,comment_username,comment_nickname,comment_time.substring(0,10),comment_content,star_number, portrait_url));//显示信息
                    }
                }
                catch ( JSONException e) {
                    e.printStackTrace();
                }
                listView.setAdapter(commentAdapter);
            }

        });





    }

    private void postComment(int id, final Tdiscuss.VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/api/get_comment_list/";
        System.out.println(url);
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("discuss_id",id);

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