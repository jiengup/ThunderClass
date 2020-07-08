package com.example.thunderclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class DiscussAdapter extends ArrayAdapter<DiscussArray> {

    private int resourceId;

    public DiscussAdapter(@NonNull Context context, int resource, @NonNull List<DiscussArray> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final DiscussArray discuss = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView title = (TextView)view.findViewById(R.id.title);
        TextView content = (TextView)view.findViewById(R.id.content);
        TextView time = (TextView)view.findViewById(R.id.discusstime);

        title.setText(discuss.getTitle());
        content.setText(discuss.getContent());
        time.setText(discuss.getDiscussTime().substring(0,10));
        //

        return view;
    }
}
