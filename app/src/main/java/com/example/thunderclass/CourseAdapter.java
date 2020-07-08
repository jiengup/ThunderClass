package com.example.thunderclass;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.LinearViewHolder> {
    private Context mContext;
    private ArrayList<CourseItem> list;
    private OnItemClickListener listener;
    private String userType, username;

    public CourseAdapter(Context context, ArrayList<CourseItem> list, String userType, String username) {
        this.mContext = context;
        this.list = list;
        this.userType = userType;
        this.username = username;
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.st1_course_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, final int position) {
        final CourseItem courseItem = list.get(position);
        holder.courseName.setText(courseItem.getCourse_name());
        holder.courseClass.setText(courseItem.get_class());
        holder.courseCreator.setText(courseItem.getCreator());
        holder.courseCode.setText("课堂暗号：" + courseItem.getCode());

        if (userType.equals("Teacher")) {
            holder.courseCode.setVisibility(View.VISIBLE);
        } else {
            holder.courseCode.setVisibility(View.INVISIBLE);
        }
        if (courseItem.getStatus() == 1) {
            holder.coursing.setVisibility(View.VISIBLE);
        } else {
            holder.coursing.setVisibility(View.INVISIBLE);
        }
        holder.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equals("Student"))
                    quitSelectDialog(courseItem.getCode(), courseItem.getCourse_name(), position);
                else if(userType.equals("Teacher"))
                    deleteSelectDialog(courseItem.getCode(), courseItem.getCourse_name(), position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView courseName, courseClass, courseCreator, courseCode;
        private Button btnQuit;
        private ImageView coursing;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.st1_course_name);
            courseClass = itemView.findViewById(R.id.st1_course_class);
            courseCreator = itemView.findViewById(R.id.st1_course_creator);
            btnQuit = itemView.findViewById(R.id.st1_quit);
            coursing = itemView.findViewById(R.id.coursing);
            courseCode = itemView.findViewById(R.id.st1_course_code);
        }
    }

    private void quitSelectDialog(final String course_code, String courseName, final int position) {
        //Toast.makeText(mContext, "确定按钮", Toast.LENGTH_LONG).show();
        //Toast.makeText(mContext, "关闭按钮", Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setIcon(R.drawable.ic_warning).setTitle("退出课堂")
                .setMessage("你确定要退出 " + courseName + " 课堂吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        quitCourse(course_code, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject response = new JSONObject(result);
                                    int code = response.getInt("code");
                                    if (code == 0) {
                                        ToastUtil.showMsg(mContext, "退出成功");
                                        list.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        ToastUtil.showMsg(mContext, "退出失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        //Toast.makeText(mContext, "确定按钮", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Toast.makeText(mContext, "关闭按钮", Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void deleteSelectDialog(final String course_code, String courseName, final int position) {
        //Toast.makeText(mContext, "确定按钮", Toast.LENGTH_LONG).show();
        //Toast.makeText(mContext, "关闭按钮", Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setIcon(R.drawable.ic_warning).setTitle("删除课堂")
                .setMessage("你确定要删除 " + courseName + " 课堂吗？\n请注意：此操作将会使得加入本课堂的学生无法再访问到课程资源！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCourse(course_code, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject response = new JSONObject(result);
                                    int code = response.getInt("code");
                                    if (code == 0) {
                                        ToastUtil.showMsg(mContext, "删除成功");
                                        list.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        ToastUtil.showMsg(mContext, "删除失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        //Toast.makeText(mContext, "确定按钮", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Toast.makeText(mContext, "关闭按钮", Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }



    private void quitCourse(String course_code, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "client/quit_course/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("username", username);
            jsonObject.put("course_code", course_code);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccessResponse(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deleteCourse(String course_code, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/" + "server/delete_course/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("username", username);
            jsonObject.put("course_code", course_code);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccessResponse(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }
}
