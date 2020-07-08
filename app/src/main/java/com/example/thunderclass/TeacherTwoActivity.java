package com.example.thunderclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meetsl.scardview.SCardView;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherTwoActivity extends AppCompatActivity {

    private TextView course_name, course_class;
    private SCardView manageMember, manageTalk, printSign, classOn;
    private String courseCode, courseName, courseClass;
    private int status;
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_two);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        course_name = findViewById(R.id.t_classonname);
        course_class = findViewById(R.id.t_classonclass);
        manageMember = findViewById(R.id.manage_number);
        manageTalk = findViewById(R.id.manage_talk);
        printSign = findViewById(R.id.print_sign);
        classOn = findViewById(R.id.t_class_on);
        ll = findViewById(R.id.ll_classing);

        Intent intent = getIntent();
        courseCode = intent.getStringExtra("course_code");
        courseName = intent.getStringExtra("course_name");
        courseClass = intent.getStringExtra("course_class");
        status = intent.getIntExtra("course_status", 0);

        if(status == 0) {
            ll.setVisibility(View.INVISIBLE);
        } else {
            ll.setVisibility(View.VISIBLE);
        }

        course_name.setText(courseName);
        course_class.setText(courseClass);

        manageMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tc3 = new Intent();
                tc3.putExtra("course_code", courseCode);
                tc3.putExtra("courseName",courseName);

                tc3.setClass(TeacherTwoActivity.this,MemberManagement.class);
                startActivity(tc3);
            }
        });
        manageTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tc3 = new Intent();
                tc3.putExtra("course_code",courseCode);
                tc3.putExtra("courseName",courseName);
                tc3.setClass(TeacherTwoActivity.this,Tdiscuss.class);
                startActivity(tc3);
            }
        });

        printSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tc3 = new Intent();
                tc3.putExtra("course_name", courseName);
                tc3.putExtra("course_code", courseCode);
                tc3.setClass(TeacherTwoActivity.this, ExportSigninTableActivity.class);
                startActivity(tc3);
            }
        });

        classOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status == 0) {
                    startClass(courseCode, new VolleyCallback() {
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
                                    intent.setClass(TeacherTwoActivity.this, ClassOnActivity.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }


    private void startClass(String course_code, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "server/start_class/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("course_code", course_code);

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


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }
}