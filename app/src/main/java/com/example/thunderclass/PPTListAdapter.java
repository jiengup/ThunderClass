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

public class PPTListAdapter extends ArrayAdapter<PPTListItem> {

    private int resourcesId;
    private OnItemClickListener listener;

    public PPTListAdapter(@NonNull Context context, int resource, List<PPTListItem> list) {
        super(context, resource, list);
        resourcesId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PPTListItem pptListItem = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourcesId, parent, false);

        TextView pptName = view.findViewById(R.id.ppt_list_name);
        TextView pptPubTime = view.findViewById(R.id.ppt_list_pub_time);
        ImageView page1 = view.findViewById(R.id.ppt_first_img);

        pptName.setText(pptListItem.getPpt_name());
        pptPubTime.setText(pptListItem.getPub_time().substring(0, 10));
        Glide.with(view).load(pptListItem.getPage1URL()).into(page1);
        return view;
    }
    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
