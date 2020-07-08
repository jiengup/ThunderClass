package com.example.thunderclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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


public class MemberManagement extends AppCompatActivity {

    private String course_code;
    private ListView listView;
    private List<String> tstudent;
    private List<ImageListArray> onePieceList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_management);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        course_code = intent.getStringExtra("course_code");

        final ImageListAdapter imageListAdapter = new ImageListAdapter(this, R.layout.member_list_item, onePieceList);
        final ListView listView = (ListView) findViewById(R.id.tstudentList); //将适配器导入Listview

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ToastUtil.showMsg(getApplicationContext(),"长按删除");
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                deleteMember(course_code, onePieceList.get(position).getUseName(), new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        try {
                            JSONObject response = new JSONObject(result);
                            int code = response.getInt("code");
                            if (code == 0) {
                                ToastUtil.showMsg(getApplicationContext(), "删除成功");
                                onePieceList.remove(position);
                                listView.setAdapter(imageListAdapter);
                            } else {
                                ToastUtil.showMsg(getApplicationContext(), "删除失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return false;
            }
        });

        postMemberList(course_code, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result)  {
                try {
                    String usename;
                    String portrait_url;
                    String nickname;
                    String classnaem;
                    JSONObject response = new JSONObject(result);
                    JSONArray memblist = response.getJSONArray("data");

                    for(int i=0;i<memblist.length();i++){
                        JSONObject member = (JSONObject) memblist.get(i);
                        usename = member.getString("username");
                        portrait_url = "http:10.0.2.2:8000" + member.getString("portrait_url");
                        nickname = member.getString("nickname");
                        classnaem = member.getString("class");

                        System.out.println(usename+' '+portrait_url+' '+nickname+' '+classnaem);
                        onePieceList.add(new ImageListArray(usename,portrait_url,nickname,classnaem));//显示信息
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

    private void deleteMember(String course_code, String username, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/server/delete_member/";
        System.out.println(url);
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("course_code", course_code);//测试
            jsonBody.put("username", username);
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