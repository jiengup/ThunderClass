package com.example.thunderclass;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.donkingliang.banner.CustomBanner;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    private LinearLayout profile;
    private CourseAdapter courseAdapter;
    private ArrayList<CourseItem> courseItems;
    private String username, userSchool, userType, portrait_url;
    private SharedPreferences mSharePreferences;
    private SharedPreferences.Editor mEditor;
    private CustomBanner<String> customBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);


        courseItems = new ArrayList<CourseItem>();
        mSharePreferences = getSharedPreferences("user", MODE_PRIVATE);
        username = mSharePreferences.getString("username", "null");
        userSchool = mSharePreferences.getString("school", "null");
        userType = mSharePreferences.getString("type", "null");
        portrait_url = mSharePreferences.getString("portrait_url", "null");
        mEditor = mSharePreferences.edit();
        System.out.println(portrait_url);
        getCourseList(username, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                System.out.println(result);
                try {
                    JSONObject response = new JSONObject(result);
                    int code = response.getInt("code");
                    if(code == 1) {
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else{
                        JSONArray data = response.getJSONArray("data");
                        for(int i=0; i<data.length(); i++) {
                            CourseItem courseItem = new CourseItem();
                            JSONObject item = data.getJSONObject(i);
                            courseItem.setCode(item.getString("course_code"));
                            courseItem.setCourse_name(item.getString("course_name"));
                            courseItem.setCredit(item.getString("credit"));
                            courseItem.setLength(item.getInt("length"));
                            //courseItem.setStatus(item.getInt("status"));
                            courseItem.setStatus(item.getInt("status"));
                            courseItem.set_class((item.getString("class")));
                            courseItem.setPubTime(item.getString("pub_time"));
                            courseItem.setCreator(item.getString("creator"));
                            courseItems.add(courseItem);
                        }
                    }
                    courseAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println(courseItems.size());
        initUI();
    }
    void initUI() {
        Toolbar toolbar = findViewById(R.id.st1_home_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.st1_drawer_layout);
        NavigationView navigationView = findViewById(R.id.st1_nav);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        profile = header.findViewById(R.id.st1_profile);
        profile.setOnClickListener(this);
        TextView st1_name = profile.findViewById(R.id.st1_profile_name);
        TextView st1_school = profile.findViewById(R.id.st1_profile_school);
        TextView st1_type = profile.findViewById(R.id.st1_profile_type);
        ImageView portrait = profile.findViewById(R.id.st1_profile_portrait);
        st1_name.setText(username);
        st1_school.setText(userSchool);
        if(userType.equals("Student")) {
            st1_type.setText("学生");
        } else{
            st1_type.setText("老师");
        }
        Glide.with(portrait).load(portrait_url).into(portrait);
        customBanner = findViewById(R.id.st1_banner);
        ArrayList<String> images = new ArrayList<>();

        images.add("http://seopic.699pic.com/photo/40010/9017.jpg_wh1200.jpg");
        images.add("http://seopic.699pic.com/photo/40007/4017.jpg_wh1200.jpg");
        images.add("http://seopic.699pic.com/photo/40061/0674.jpg_wh1200.jpg");
        images.add("http://seopic.699pic.com/photo/40058/3289.jpg_wh1200.jpg");
        images.add("http://seopic.699pic.com/photo/40005/9002.jpg_wh1200.jpg");
        images.add("http://seopic.699pic.com/photo/40053/8466.jpg_wh1200.jpg");
        images.add("http://seopic.699pic.com/photo/40072/2728.jpg_wh1200.jpg");
        setBean(images);
        RecyclerView recyclerView = findViewById(R.id.st1_rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MyDecoration());
        courseAdapter = new CourseAdapter(this, courseItems, userType, username);
        recyclerView.setAdapter(courseAdapter);
        courseAdapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("course_code", courseItems.get(position).getCode());
                intent.putExtra("course_name", courseItems.get(position).getCourse_name());
                intent.putExtra("course_class", courseItems.get(position).get_class());
                intent.putExtra("course_status", courseItems.get(position).getStatus());
                intent.setClass(StudentHomeActivity.this, St2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    //菜单栏选中
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.st1_nav_classpassword) {
            inputTitleDialog();
        } else if(id == R.id.st1_nav_scan) {
            //TODO: 二维码扫描加入课堂功能
        } else if(id == R.id.st1_nav_logout) {
            mEditor.clear();
            mEditor.commit();
            Intent intent = new Intent(StudentHomeActivity.this, UserLoginActivity.class);
            startActivity(intent);
        }
        return true;
    }

    //跳转到个人信息页面
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileDetailActivity.class);
        startActivity(intent);
    }

    private void getCourseList(String username, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "api/get_course_list/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();

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

    private void setBean(final ArrayList<String> beans) {
        customBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans)
//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);
    }
    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }
    public class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, 0);
        }
    }

    private void inputTitleDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入课堂暗号加入课堂").setView(inputServer).
                setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String code = inputServer.getText().toString();
                        joinCourse(code, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try{
                                    JSONObject response = new JSONObject(result);
                                    int code = response.getInt("code");
                                    if(code == 0) {
                                        ToastUtil.showMsg(getApplicationContext(), "加入成功");
                                        getCourseList(username, new VolleyCallback() {

                                            @Override
                                            public void onSuccessResponse(String result) {
                                                try {
                                                    JSONObject response = new JSONObject(result);
                                                    int code = response.getInt("code");
                                                    if(code == 1) {
                                                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                                                    } else{
                                                        JSONArray data = response.getJSONArray("data");
                                                        courseItems.clear();
                                                        for(int i=0; i<data.length(); i++) {
                                                            CourseItem courseItem = new CourseItem();
                                                            JSONObject item = data.getJSONObject(i);
                                                            courseItem.setCode(item.getString("course_code"));
                                                            courseItem.setCourse_name(item.getString("course_name"));
                                                            courseItem.setCredit(item.getString("credit"));
                                                            courseItem.setLength(item.getInt("length"));
                                                            courseItem.setStatus(item.getInt("status"));
                                                            courseItem.set_class((item.getString("class")));
                                                            courseItem.setPubTime(item.getString("pub_time"));
                                                            courseItem.setCreator(item.getString("creator"));
                                                            courseItems.add(courseItem);
                                                        }
                                                    }
                                                    courseAdapter.notifyDataSetChanged();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } else if(code == 1) {
                                        ToastUtil.showMsg(getApplicationContext(), "暗号错误");
                                    } else if(code == 2) {
                                        ToastUtil.showMsg(getApplicationContext(), "加入失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
        builder.show();
    }

    private void joinCourse(String code, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "client/join_course_code/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("username", username);
            jsonObject.put("code", code);

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



}
