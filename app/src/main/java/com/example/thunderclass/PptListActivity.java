package com.example.thunderclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class PptListActivity extends AppCompatActivity {

    private String course_code, username, userType;
    private ListView listView;
    private TextView title;
    private List<PPTListItem> pptListItemArrayList;
    private PPTListAdapter pptListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_t_course_p_p_t);
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userType = sharedPreferences.getString("type", "null");

        if(userType.equals("Teacher")) {
            username = sharedPreferences.getString("username", "null");
            title = findViewById(R.id.ppt_list_title);
            title.setText("我的课件");
        } else {
            course_code = intent.getStringExtra("course_code");
        }

        pptListItemArrayList = new ArrayList<PPTListItem>();

        listView = findViewById(R.id.ppt_list);
        pptListAdapter = new PPTListAdapter(this, R.layout.ppt_list_item, pptListItemArrayList);

        getPPTList(course_code, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray items = jsonObject.getJSONArray("data");
                    for(int i=0; i<items.length(); i++) {
                        JSONObject item = (JSONObject)items.get(i);
                        PPTListItem pptListItem = new PPTListItem();
                        pptListItem.setId(item.getInt("ppt_id"));
                        pptListItem.setPpt_name(item.getString("ppt_name"));
                        pptListItem.setPub_time(item.getString("pub_time"));
                        pptListItem.setPage1URL("http://10.0.2.2:8000" + item.getString("page1_url"));
                        pptListItemArrayList.add(pptListItem);
                    }
                    listView.setAdapter(pptListAdapter);
                } catch (JSONException  e) {
                    e.printStackTrace();
                }
            }
        });

        String task = intent.getStringExtra("task");
        if(task != null && task.equals("push_ppt")) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intentRet = new Intent();
                    intentRet.putExtra("ppt_id", pptListItemArrayList.get(position).getId());
                    setResult(1, intentRet);
                    finish();
                }
            });
        } else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(PptListActivity.this, PPTDetailActivity.class);
                    System.out.println("clicking: " + pptListItemArrayList.get(position).getPpt_name() + pptListItemArrayList.get(position).getId());
                    intent.putExtra("ppt_name", pptListItemArrayList.get(position).getPpt_name());
                    intent.putExtra("ppt_id", pptListItemArrayList.get(position).getId());
                    startActivity(intent);
                }
            });
        }
    }

    private void getPPTList(String course_code, final VolleyCallback callback) {
        String url = "";
        if(userType.equals("Student"))
            url = "http://10.0.2.2:8000/client/get_course_ppts/";
        else
            url = "http://10.0.2.2:8000/server/get_ppt_list/";
        System.out.println(url);
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            if(userType.equals("Student"))
                jsonBody.put("course_code", course_code);//测试
            else
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
