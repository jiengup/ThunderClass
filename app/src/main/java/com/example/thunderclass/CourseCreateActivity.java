package com.example.thunderclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.Result;


public class CourseCreateActivity extends AppCompatActivity {

    private EditText etCourseName, etCredit, etLength, etClass;
    private Button create;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        etCourseName = findViewById(R.id.et_course_name);
        etCredit = findViewById(R.id.et_credit);
        etLength = findViewById(R.id.et_length);
        etClass = findViewById(R.id.et_class);
        create = findViewById(R.id.btn_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCourse(username, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        System.out.println(result);
                        try {
                            JSONObject response = new JSONObject(result);
                            int code = response.getInt("code");
                            if(code == 1) {
                                ToastUtil.showMsg(getApplicationContext(), "创建失败");
                            } else {
                                String courseCode = response.getString("course_code");
                                //ToastUtil.showMsg(getApplicationContext(), response.getString("course_code"));
                                Intent intent = new Intent();
                                intent.putExtra("course_name", etCourseName.getText().toString());
                                intent.putExtra("course_code", courseCode);
                                intent.putExtra("username", username);
                                intent.setClass(CourseCreateActivity.this, CreateResultActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    private void createCourse(String username, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "server/create_course/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("username", username);
            jsonObject.put("course_name", etCourseName.getText().toString());
            jsonObject.put("credit", etCredit.getText().toString());
            jsonObject.put("length", etLength.getText().toString());
            jsonObject.put("class", etClass.getText().toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
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
