package com.example.thunderclass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ViewMembers extends AppCompatActivity {

    private String course_code;
    private ListView listView;
    private List<String> student;
    private List<ImageListArray> onePieceList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        course_code = intent.getStringExtra("course_code");

        final ImageListAdapter imageListAdapter = new ImageListAdapter(this, R.layout.member_list_item, onePieceList);
        final ListView listView = (ListView) findViewById(R.id.studentList); //将适配器导入Listview


        postMemberList(course_code, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result)  {
                try {
                    String usename;
                    String portrait_url;
                    String nickname;
                    String classname;
                    JSONObject response = new JSONObject(result);
                    JSONArray memblist = response.getJSONArray("data");

                    for(int i=0;i<memblist.length();i++){
                        JSONObject member = (JSONObject) memblist.get(i);
                        usename = member.getString("username");
                        portrait_url = member.getString("portrait_url");
                        nickname = member.getString("nickname");
                        classname = member.getString("class");

                        System.out.println(usename+' '+portrait_url+' '+nickname+' '+ classname);
                        onePieceList.add(new ImageListArray(usename, "http://10.0.2.2:8000"+portrait_url, nickname, classname));//显示信息
                    }
                    listView.setAdapter(imageListAdapter);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
      }

    private void postMemberList(String course_code, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/api/get_course_members/";
        System.out.println(url);
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("course_code", course_code);//测试
            //jsonBody.put("course_code", course_code);

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
