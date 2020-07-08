package com.example.thunderclass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.thunderclass.utils.ToastUtil;

import org.json.JSONObject;

public class UserSginUpActivity extends AppCompatActivity {

    private EditText s_name;
    private EditText s_nick;
    private EditText s_school;
    private EditText s_email;
    private EditText s_pass;
    private RadioGroup s_type;
    private Button sign_up;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sgin_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        s_name=findViewById(R.id.username);
        s_nick=findViewById(R.id.nickname);
        s_school=findViewById(R.id.school);
        s_email=findViewById(R.id.email);
        s_pass=findViewById(R.id.password);
        s_type=findViewById(R.id.usertype);
        sign_up=findViewById(R.id.btn_sign_up);

        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=s_name.getText().toString();
                String nickname=s_nick.getText().toString();
                String school=s_school.getText().toString();
                String email=s_email.getText().toString();
                String password=s_pass.getText().toString();
                String user_type="";
                int t=s_type.getCheckedRadioButtonId();
                if(t==R.id.tea){
                    user_type="Teacher";
                    ToastUtil.showMsg(getApplicationContext(),"Teacher");
                }
                if(t==R.id.stu){
                    user_type="Student";
                    ToastUtil.showMsg(getApplicationContext(),"Student");
                }

                //判断信息
                if(username.equals("")){
                    ToastUtil.showMsg(getApplicationContext(),"请输入用户名");
                }
                else if(nickname.equals("")){
                    ToastUtil.showMsg(getApplicationContext(),"请输入昵称");
                }
                else if(school.equals("")){
                    ToastUtil.showMsg(getApplicationContext(),"请输入学校");
                }
                else if(email.equals("")){
                    ToastUtil.showMsg(getApplicationContext(),"请输入邮箱");
                }
                else if(password.equals("")){
                    ToastUtil.showMsg(getApplicationContext(),"请输入密码");
                }
                else if(password.length()<6){
                    ToastUtil.showMsg(getApplicationContext(),"请输入大于6位的密码");
                }
                else{
                    register(username, nickname, school, email, password, user_type, new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            try{
                                JSONObject response=new JSONObject(result);
                                int code = response.getInt("code");
                                String msg=response.getString("msg");
                                ToastUtil.showMsg(getApplicationContext(), msg);
                                Intent intent=new Intent(getApplicationContext(),UserLoginActivity.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    finish();
                }
            }
        });

    }

    public void register(String username,String nickname,String school,String email,String password,String user_type,final VolleyCallback callback){
        //String url = Constants.MYAUTH_URL + "register/";
        String url = "http://10.0.2.2:8000/account/register/";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("username",username);
            jsonBody.put("nickname",nickname);
            jsonBody.put("school",school);
            jsonBody.put("email",email);
            jsonBody.put("password",password);
            jsonBody.put("user_type",user_type);
            jsonBody.put("belong_to_class","1");

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject jsonObject) {
                    callback.onSuccessResponse(jsonObject.toString());
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("onErrorResponse", volleyError.toString());
                    Toast.makeText(getApplicationContext(), "Error: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            System.out.println(jsonBody.toString());
            queue.add(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public interface VolleyCallback{
        void onSuccessResponse(String result);
    }
}
