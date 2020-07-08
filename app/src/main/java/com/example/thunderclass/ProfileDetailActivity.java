package com.example.thunderclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProfileDetailActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TextView nickname, username, courseNum, school, _class, email;
    private ImageView portrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        nickname = findViewById(R.id.profile_nickname);
        username = findViewById(R.id.profile_username);
        courseNum = findViewById(R.id.profile_course_num);
        school = findViewById(R.id.profile_school);
        _class = findViewById(R.id.profile_class);
        email = findViewById(R.id.profile_email);
        portrait = findViewById(R.id.profile_portrait);

        nickname.setText(sharedPreferences.getString("nickname", "null"));
        username.setText(sharedPreferences.getString("username", "null"));
        school.setText(sharedPreferences.getString("school", "null"));
        _class.setText(sharedPreferences.getString("class", "null"));
        courseNum.setText(sharedPreferences.getString("course_num", "0"));
        email.setText(sharedPreferences.getString("email", "null"));
        Glide.with(getApplicationContext()).load(sharedPreferences.getString("portrait_url", "")).into(portrait);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
