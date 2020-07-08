package com.example.thunderclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {

    private ExerciseAdapter exerciseAdapter;
    private RecyclerView rcv;
    private ArrayList<ExerciseItem> exerciseItems;
    private String userType, username, courseCode;
    private TextView title;
    private String task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        exerciseItems = new ArrayList<ExerciseItem>();
        Intent intent = getIntent();

        task = intent.getStringExtra("task");
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        userType = sharedPreferences.getString("type", "null");

        if(userType.equals("Teacher")) {
            username = sharedPreferences.getString("username", "null");
            title = findViewById(R.id.exercise_title);
            title.setText("我的试题");
        } else {
            courseCode = intent.getStringExtra("course_code");
        }
        getExerciseList(new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                //System.out.println(result);
                try {
                    JSONObject response = new JSONObject(result);
                    int code = response.getInt("code");
                    if(code == 1) {
                        ToastUtil.showMsg(getApplicationContext(), "获取失败");
                    } else {
                        JSONArray items = response.getJSONArray("data");
                        for(int i=0; i<items.length(); i++) {
                            JSONObject item = (JSONObject) items.get(i);
                            ExerciseItem exerciseItem = new ExerciseItem();
                            exerciseItem.setId(item.getInt("exercise_id"));
                            exerciseItem.setPubTime(item.getString("pub_time").substring(0, 10));
                            int type = item.getInt("exercise_type");
                            exerciseItem.setType(type);
                            if(type == 0) {
                                exerciseItem.setContent(item.getString("content"));
                            } else {
                                exerciseItem.setContent(item.getString("content"));
                                exerciseItem.setPubTime(item.getString("pub_time").substring(0, 10));
                                exerciseItem.setChoice_num(item.getInt("choice_num"));
                                exerciseItem.setChoiceA(item.getString("choice_A"));
                                exerciseItem.setChoiceB(item.getString("choice_B"));
                                exerciseItem.setChoiceC(item.getString("choice_C"));
                                exerciseItem.setChoiceD(item.getString("choice_D"));
                                exerciseItem.setCorrectAns(item.getInt("correct_ans"));
                            }
                            exerciseItems.add(exerciseItem);
                        }
                    }
                    exerciseAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        exerciseAdapter = new ExerciseAdapter(this, exerciseItems, task);
        rcv = findViewById(R.id.exercise_rcv);
        rcv.addItemDecoration(new MyDecoration());
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(exerciseAdapter);
        exerciseAdapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intentRet = new Intent();
                intentRet.putExtra("exercise_id", exerciseItems.get(position).getId());
                setResult(2, intentRet);
                finish();
            }
        });
    }

    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }

    public class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, 0);
        }
    }

    private void getExerciseList(final VolleyCallback callback) {
        String url = "";
        if(userType.equals("Student"))
            url = "http://10.0.2.2:8000/" + "client/get_course_exercises/";
        else
            url = "http://10.0.2.2:8000/" + "server/get_exercise_list/";
        //System.out.println(url);
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();
            System.out.println(username + courseCode);
            if(userType.equals("Student"))
                jsonObject.put("course_code", courseCode);
            else
                jsonObject.put("username", username);
            System.out.print(jsonObject.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccessResponse(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
