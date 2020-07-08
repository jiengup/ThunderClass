package com.example.thunderclass;

import android.content.Context;
import android.content.Intent;
import android.os.TestLooperManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class PPTDetailAdapter extends ArrayAdapter<String> {

    private int resourcesId;

    public PPTDetailAdapter(@NonNull Context context, int resource, List<String> list) {
        super(context, resource, list);
        resourcesId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String pageUrl = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourcesId, parent, false);
        ImageView page = view.findViewById(R.id.ppt_page);
        Glide.with(view).load(pageUrl).into(page);
        return view;
    }



}
