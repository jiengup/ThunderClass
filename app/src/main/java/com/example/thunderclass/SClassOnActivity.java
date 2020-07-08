package com.example.thunderclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class SClassOnActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SFragmentPagerAdapter mFragmentPagerAdapter;
    private TabLayout.Tab ppt;
    private TabLayout.Tab test;
    private String courseName, courseClass, courseCode, username;
    private int classroomID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_class_on);
        mTabLayout=findViewById(R.id.tab_s_classon);
        mViewPager=findViewById(R.id.vp_s_classon);
        Intent intent = getIntent();
        courseName = intent.getStringExtra("course_name");
        courseClass = intent.getStringExtra("course_class");
        courseCode = intent.getStringExtra("course_code");
        classroomID = intent.getIntExtra("classroom_id", 0);
        System.out.println(classroomID);


        TextView classingName = findViewById(R.id.s_classonname);
        TextView classingClass = findViewById(R.id.s_classonclass);

        classingName.setText(courseName);
        classingClass.setText(courseClass);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "null");
        mFragmentPagerAdapter=new SFragmentPagerAdapter(getSupportFragmentManager(), classroomID);
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager.setAdapter(mFragmentPagerAdapter);
        //将TabLayout和ViewPager绑定在一起，相互影响，解放了开发人员对双方变动事件的监听
        mTabLayout.setupWithViewPager(mViewPager);
        //指定Tab的位置
        ppt=mTabLayout.getTabAt(0);
        test=mTabLayout.getTabAt(1);


    }
}