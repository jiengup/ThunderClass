package com.example.thunderclass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<CommentArray> {

    private int resourceId;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<CommentArray> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final CommentArray comment = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView content = (TextView)view.findViewById(R.id.comment_content);
        TextView commenttime = (TextView)view.findViewById(R.id.comment_time);
        TextView nickName = (TextView)view.findViewById(R.id.comment_item_nick_name);
        TextView userName = (TextView)view.findViewById(R.id.comment_item_user_name);
        ImageView portrait = (ImageView)view.findViewById(R.id.comment_portrait);

        Glide.with(view).load(comment.getPortrait_url()).into(portrait);

        final TextView starNumber = (TextView)view.findViewById(R.id.comment_item_star_num);
        starNumber.setText("("+new Integer(comment.getStarNumber()).toString()+")");
        nickName.setText(comment.getNickname());
        userName.setText(comment.getUsename());
        commenttime.setText(comment.getCommentTime().substring(0,10));
        content.setText(comment.getContent());

        ShineButton heart = (ShineButton) view.findViewById(R.id.comment_item_bt_heart);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starNumber.setText("(" + new Integer(comment.getStarNumber() + 1).toString() + ")");
                String url = "http://10.0.2.2:8000/api/modify_star_num/";
                try {
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    JSONObject jsonBody = new JSONObject();

                    jsonBody.put("comment_id", comment.getId());
                    jsonBody.put("comment_star_num", comment.getStarNumber()+1);
                    System.out.println(comment.getId());

                    JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("onResponse", jsonObject.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("onErrorResponse", volleyError.toString());
                        }
                    });
                    queue.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
