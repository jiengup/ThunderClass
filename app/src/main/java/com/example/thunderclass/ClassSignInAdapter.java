package com.example.thunderclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ClassSignInAdapter extends ArrayAdapter<ClassSignItem> {

    private int resourceId;
    private String courseName;

    public ClassSignInAdapter(@NonNull Context context, int resource, List<ClassSignItem> objects, String courseName) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.courseName = courseName;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassSignItem classSignItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView title = view.findViewById(R.id.class_signin_title);
        TextView time = view.findViewById(R.id.class_signin_time);

        title.setText(courseName + "第" + classSignItem.getNum() + "次课");
        time.setText(classSignItem.getClassOnTime().substring(0, 16));
        return view;
    }
}
