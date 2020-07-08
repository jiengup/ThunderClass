package com.example.thunderclass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meetsl.scardview.SCardView;

import org.json.JSONException;
import org.json.JSONObject;

public class St2 extends AppCompatActivity {

    private String courseCode;
    private String courseName;
    private String courseClass;
    private TextView className, course_class;
    private SCardView viewMember, viewCoursewares, reviwQuestions, discuss;
    private int status;
    private LinearLayout ll;
    private Button classOn;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Intent st1 = getIntent();//接受参数
        courseCode = st1.getStringExtra("course_code");
        courseName = st1.getStringExtra("course_name");
        courseClass = st1.getStringExtra("course_class");
        status = st1.getIntExtra("course_status", 0);
        System.out.println(courseName);
        className = findViewById (R.id.course_name);//设置课程名称
        className.setText(courseName);
        course_class = findViewById(R.id.course_class);
        course_class.setText(courseClass);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "null");
        //className.setText("微积分");//测试
        classOn = findViewById(R.id.s_class_on);//上课


        ll = findViewById(R.id.s_ll_classing);

        if(status == 0) {
            ll.setVisibility(View.INVISIBLE);
        } else {
            ll.setVisibility(View.VISIBLE);
        }

        classOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    enterClass(courseCode, new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                int code = response.getInt("code");
                                if(code == 0) {
                                    int classId = response.getInt("classroom_id");
                                    Intent intent = new Intent();
                                    intent.putExtra("course_name", courseName);
                                    intent.putExtra("course_class", courseClass);
                                    intent.putExtra("course_code", courseCode);
                                    intent.putExtra("classroom_id", classId);
                                    intent.setClass(St2.this, ClassOnActivity.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            }
        });

        viewMember = findViewById(R.id.class_mumber);//查看成员按钮
        viewMember.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("course_code",courseCode);

                intent.setClass(St2.this,ViewMembers.class);
                startActivity(intent);
            }
        });

        viewCoursewares = findViewById(R.id.scan_ppt);//查看课件按钮
        viewCoursewares.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("course_code", courseCode);
                intent.setClass(St2.this, PptListActivity.class);
                startActivity(intent);
            }
        });

        reviwQuestions = findViewById(R.id.scan_test);//回顾问题按钮
        reviwQuestions.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("course_code", courseCode);
                intent.setClass(St2.this, ExerciseListActivity.class);
                startActivity(intent);
            }
        });
//
        discuss = findViewById(R.id.class_talk);//讨论按钮
        discuss.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("course_code", courseCode);
                intent.setClass(St2.this, Sdiscuss.class);
                startActivity(intent);
            }
        });
//



  /*      test = findViewById(R.id.discuss);//tc的学生管理（测试）
        test.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("course_code",course_code);

                intent.setClass(St2.this,MemberManagement.class);
                startActivity(intent);
            }
        });

   */
    }
    private void enterClass(String course_code, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "client/enter_class/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("course_code", course_code);
            jsonObject.put("username", username);

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