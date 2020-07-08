package com.example.thunderclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CreateResultActivity extends AppCompatActivity {

    private TextView courseName, courseCode, userName;
    private ImageView QRCode;
    private Button btn_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        courseCode = findViewById(R.id.result_course_code);
        courseName = findViewById(R.id.result_course_name);
        userName = findViewById(R.id.result_profile_nickname);
        courseName.setText(intent.getStringExtra("course_name"));
        courseCode.setText(intent.getStringExtra("course_code"));
        userName.setText(intent.getStringExtra("username"));
        btn_finish = findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateResultActivity.this, TeacherHomeActivity.class);
                startActivity(intent);
            }
        });
        QRCode = findViewById(R.id.result_QRcode);
        QRCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO:长按保存
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
