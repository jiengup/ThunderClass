package com.example.thunderclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class ExportSigninTableActivity extends AppCompatActivity {

    private String course_code, courseName;
    private ListView listView;
    private List<ClassSignItem> list = new ArrayList<ClassSignItem>();
    private ClassSignInAdapter classSignInAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_signin_table);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        course_code = intent.getStringExtra("course_code");
        courseName = intent.getStringExtra("course_name");

        TextView title = findViewById(R.id.signin_title);
        title.setText(courseName + "课堂");

        listView = findViewById(R.id.signin_list);
        classSignInAdapter = new ClassSignInAdapter(this, R.layout.class_signin_item, list, courseName);
        get_course_class(course_code, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result)  {
                System.out.println(result);
                try {
                    JSONObject response = new JSONObject(result);
                    int code = response.getInt("code");
                    if(code == 0) {
                        JSONArray items = response.getJSONArray("data");
                        int cnt = 1;
                        for(int i=0; i<items.length(); i++) {
                            ClassSignItem classSignItem = new ClassSignItem();
                            JSONObject item = (JSONObject) items.get(i);
                            classSignItem.setId(item.getInt("class_id"));
                            classSignItem.setNum(cnt++);
                            classSignItem.setClassOnTime(item.getString("create_time"));
                            classSignItem.setEndTime(item.getString("end_time"));
                            list.add(classSignItem);
                        }
                        listView.setAdapter(classSignInAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("class_name", courseName + "第" + (position+1) + "次课");
                intent.putExtra("class_id", list.get(position).getId());
                intent.putExtra("start_time", list.get(position).getClassOnTime());
                intent.putExtra("end_time", list.get(position).getEndTime());
                intent.setClass(ExportSigninTableActivity.this, SignupTableActivity.class);
                startActivity(intent);
            }
        });
    }

    private void get_course_class(String course_code, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/server/get_course_class/";
        System.out.println(url);
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("course_code", course_code);
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
