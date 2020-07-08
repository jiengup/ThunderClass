package com.example.thunderclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class SignupTableActivity extends AppCompatActivity {

    private String className, startTime, endTime;
    private int classId;
    private ListView listView;
    private List<SignupTableItem> list = new ArrayList<SignupTableItem>();
    private SignupTableAdapter signupTableAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_table);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        className = intent.getStringExtra("class_name");
        classId = intent.getIntExtra("class_id", 0);
        startTime = intent.getStringExtra("start_time");
        endTime = intent.getStringExtra("end_time");

        TextView class_name = findViewById(R.id.table_title);
        class_name.setText(className);
        TextView start_time = findViewById(R.id.start_time);
        TextView end_time = findViewById(R.id.end_time);

        start_time.setText("开始时间：" + startTime);
        end_time.setText("结束时间：" + endTime);


        listView = findViewById(R.id.table_list);

        signupTableAdapter = new SignupTableAdapter(this, R.layout.sign_up_table_item, list);

        getSignUpTable(classId, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    int code = response.getInt("code");
                    if(code == 0) {
                        JSONArray items = response.getJSONArray("data");
                        for(int i=0; i<items.length(); i++) {
                            JSONObject item = (JSONObject) items.get(i);
                            SignupTableItem signupTableItem = new SignupTableItem();
                            signupTableItem.setNickname(item.getString("nickname"));
                            signupTableItem.setUsername(item.getString("username"));
                            signupTableItem.setEnterTime(item.getString("enter_time"));
                            signupTableItem.setLeaveTime(item.getString("leave_time"));
                            signupTableItem.setDeltaTime(item.getString("delta_time"));
                            list.add(signupTableItem);
                        }
                        listView.setAdapter(signupTableAdapter);
                    } else {
                        ToastUtil.showMsg(getApplicationContext(), "获取失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getSignUpTable(int class_id, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/server/get_signup_table/";
        System.out.println(url);
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("class_id", class_id);
            //jsonBody.put("course_code", course_code);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                        callback.onSuccessResponse(jsonObject.toString());
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
        void onSuccessResponse(String result);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
