package com.example.thunderclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class SignupTableAdapter extends ArrayAdapter<SignupTableItem> {

    private int resourceId;

    public SignupTableAdapter(@NonNull Context context, int resource, @NonNull List<SignupTableItem> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SignupTableItem signupTableItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView username = view.findViewById(R.id.sign_up_username);
        TextView nickname = view.findViewById(R.id.sign_up_nickname);
        TextView enter_time = view.findViewById(R.id.sign_up_enter);
        TextView leave_time = view.findViewById(R.id.sign_up_leave);
        TextView delta_time = view.findViewById(R.id.sign_up_delta);

        username.setText(signupTableItem.getUsername());
        nickname.setText(signupTableItem.getNickname());
        enter_time.setText(signupTableItem.getEnterTime());
        leave_time.setText(signupTableItem.getLeaveTime());
        delta_time.setText(signupTableItem.getDeltaTime());

        return view;
    }


}
