package com.example.thunderclass;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.example.thunderclass.utils.ToastUtil;
import com.example.thunderclass.utils.ValidateUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLoginActivity extends AppCompatActivity {

    private EditText l_name;
    private EditText l_password;
    private TextView forget_pass;
    private TextView sign_up;
    private Button   blogin;

    private SharedPreferences mSharePreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        l_name = findViewById(R.id.u_name);
        l_password = findViewById(R.id.u_password);
        forget_pass = findViewById(R.id.forgot_password);
        sign_up = findViewById(R.id.sign_up);
        blogin = findViewById(R.id.btn_login);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(UserLoginActivity.this,UserSginUpActivity.class);
                startActivity(intent);
            }
        });


        mSharePreferences = getSharedPreferences("user", MODE_PRIVATE);
        mEditor = mSharePreferences.edit();
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = l_name.getText().toString();
                String password = l_password.getText().toString();
                ToastUtil.showMsg(getApplicationContext(), username + "|" + password);
                if(username.equals(""))
                {
                    ToastUtil.showMsg(getApplicationContext(), "请输入用户名");
                }
                else if(password.equals(""))
                {
                    ToastUtil.showMsg(getApplicationContext(),"请输入密码");
                }
                else
                {
                    //Intent intent = new Intent(getApplicationContext(),StudentHomeActivity.class);
                    //startActivity(intent);
                    login(username,password,new VolleyCallback(){
                        @Override
                        public void onSuccessResponse(String result) {
                            try{
                                JSONObject response = new JSONObject(result);
                                int code = response.getInt("code");
                                System.out.println(code);
                                if(code!=0){
                                    String msg = response.getString("msg");
                                    System.out.println(msg);
                                    ToastUtil.showMsg(getApplicationContext(), msg);
                                }
                                else{
                                    JSONObject data = response.getJSONObject("data");
                                    String loginname=data.getString("username");
                                    System.out.println(loginname);
                                    String portrait_url="http://10.0.2.2:8000" + data.getString("portrait_url");
                                    String logintype=data.getString("user_type");
                                    //System.out.println(logintype);
                                    String loginnick=data.getString("nickname");
                                    //System.out.println(loginnick);
                                    String loginschool=data.getString("school");
                                    //System.out.println(loginschool);
                                    String loginemail=data.getString("email");
                                    //System.out.println(loginemail)
                                    int logincoursenum = data.getInt("course_num");
                                    ToastUtil.showMsg(getApplicationContext(), "登陆成功");
                                    mEditor.putString("username",loginname);
                                    mEditor.putString("portrait_url",portrait_url);
                                    mEditor.putString("type",logintype);
                                    mEditor.putString("nickname",loginnick);
                                    mEditor.putString("school",loginschool);
                                    mEditor.putString("email",loginemail);
                                    mEditor.putString("course_num", String.valueOf(logincoursenum));
                                    if(logintype.equals("Student")){
                                        String loginclass=data.getString("class");
                                        System.out.println(loginclass);
                                        mEditor.putString("class",loginclass);
                                    }
                                    mEditor.apply();
                                    if(logintype.equals("Student")){
                                        Intent intent = new Intent(getApplicationContext(),StudentHomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
                                        startActivity(intent);
                                    }

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

    private void login(String username,String password,final VolleyCallback callback){
        //String url = Constants.MYAUTH_URL + "login/";
        //String url = "http://127.0.0.1:8000/account/login/";
        String url = "http://10.0.2.2:8000/account/login/";
        //String url = "http://192.168.0.105:8000/account/login/";
        System.out.println(url);
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonbody = new JSONObject();

            jsonbody.put("username",username);
            jsonbody.put("password",password);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonbody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    callback.onSuccessResponse(jsonObject.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("onError",volleyError.toString());
                    Toast.makeText(getApplicationContext(),"Error: "+volleyError.toString(),Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonObject);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }

    protected void onDestroy() {

        super.onDestroy();
    }

}
