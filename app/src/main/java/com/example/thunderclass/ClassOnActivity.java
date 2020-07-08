package com.example.thunderclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.reflect.KCallable;

public class ClassOnActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private myFragmentPagerAdapter mFragmentPagerAdapter;
    private TabLayout.Tab ppt;
    private TabLayout.Tab test;
    private Button choice;
    private String courseName, courseClass, courseCode;
    private int classroomID;
    private LinearLayout ll;
    private String userType, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_on);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        courseName = intent.getStringExtra("course_name");
        courseClass = intent.getStringExtra("course_class");
        courseCode = intent.getStringExtra("course_code");
        classroomID = intent.getIntExtra("classroom_id", 0);
        System.out.println(classroomID);

        TextView classingName = findViewById(R.id.t_classing_name);
        TextView classingClass = findViewById(R.id.t_classing_class);
        ll = findViewById(R.id.btn_more);
        userType = getSharedPreferences("user", MODE_PRIVATE).getString("type", "null");
        userName = getSharedPreferences("user", MODE_PRIVATE).getString("username", "null");
        if (userType.equals("Student")) {
            ll.setVisibility(View.INVISIBLE);
            ll.setClickable(false);
        }

        classingName.setText(courseName);
        classingClass.setText(courseClass);

        mTabLayout=findViewById(R.id.tab_classon);
        mViewPager=findViewById(R.id.vp_classon);

        choice=findViewById(R.id.classonchoice);

        mFragmentPagerAdapter=new myFragmentPagerAdapter(getSupportFragmentManager(), classroomID);
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager.setAdapter(mFragmentPagerAdapter);
        //将TabLayout和ViewPager绑定在一起，相互影响，解放了开发人员对双方变动事件的监听
        mTabLayout.setupWithViewPager(mViewPager);
        //指定Tab的位置
        ppt=mTabLayout.getTabAt(0);
        test=mTabLayout.getTabAt(1);

        //菜单按钮监听
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(choice);
            }
        });
    }

    private void showPopupMenu(View view){
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.t_class, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.end_class:
                        endClass(courseCode, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject response = new JSONObject(result);
                                    int code = response.getInt("code");
                                    if(code == 0) {
                                        ToastUtil.showMsg(getApplicationContext(), "结束成功");
                                        Intent intent = new Intent();
                                        intent.setClass(ClassOnActivity.this, TeacherHomeActivity.class);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case R.id.add_ppt:
                        Intent intent = new Intent();
                        intent.putExtra("task", "push_ppt");
                        intent.setClass(ClassOnActivity.this, PptListActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.add_exercise:
                        Intent intent2 = new Intent();
                        intent2.putExtra("task", "push_exercise");
                        intent2.setClass(ClassOnActivity.this, ExerciseListActivity.class);
                        startActivityForResult(intent2, 1);
                        break;
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }

    private void endClass(String course_code, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "server/end_class/";
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

    private void leaveClass(String userName, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "client/leave_class/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("username", userName);
            jsonObject.put("class_id", classroomID);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            int sendPPTID = data.getIntExtra("ppt_id", 0);
            if(sendPPTID != 0) {
                addPPT(sendPPTID, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        try {
                            JSONObject response = new JSONObject(result);
                            int code = response.getInt("code");
                            if(code == 0) {
                                ToastUtil.showMsg(getApplicationContext(), "推送成功");
                            } else {
                                ToastUtil.showMsg(getApplicationContext(), "推送失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else if(resultCode == 2) {
            int sendExerciseID = data.getIntExtra("exercise_id", 0);
            System.out.println(sendExerciseID);
            if(sendExerciseID != 0) {
                addExercise(sendExerciseID, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        try {
                            JSONObject response = new JSONObject(result);
                            int code = response.getInt("code");
                            if(code == 0) {
                                ToastUtil.showMsg(getApplicationContext(), "推送成功");
                            } else {
                                ToastUtil.showMsg(getApplicationContext(), "推送失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }


    private void addPPT(int ppt_id, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "server/add_ppt/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("ppt_id", ppt_id);
            jsonObject.put("class_id", classroomID);

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

    private void addExercise(int exercise_id, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "server/add_exercise/";
        System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("exercise_id", exercise_id);
            jsonObject.put("class_id", classroomID);

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

    @Override
    public void onPause() {
        if(userType.equals("Student")) {
            leaveClass(userName, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONObject response = new JSONObject(result);
                        int code = response.getInt("code");
                        if(code == 0) {
                            ToastUtil.showMsg(getApplicationContext(), "你已退出课堂");
                            Intent intent = new Intent();
                            intent.setClass(ClassOnActivity.this, StudentHomeActivity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if(userType.equals("Student")) {
            leaveClass(userName, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONObject response = new JSONObject(result);
                        int code = response.getInt("code");
                        if(code == 0) {
                            ToastUtil.showMsg(getApplicationContext(), "你已退出课堂");
                            Intent intent = new Intent();
                            intent.setClass(ClassOnActivity.this, StudentHomeActivity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}