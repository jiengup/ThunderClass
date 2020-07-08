package com.example.thunderclass;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link t_test#newInstance} factory method to
 * create an instance of this fragment.
 */
public class t_test extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static private Handler handler;
    private Handler handlerDelay = new Handler();
    private boolean pptPushed = false;
    private int class_id;
    private ArrayList<ExerciseItem> exerciseItems;
    private ExerciseAdapter exerciseAdapter;
    private RecyclerView rcv;
    private RequestQueue queue;
    private int receivedNum = 0;
    public t_test() {
        // Required empty public constructor
    }
    public t_test(int class_id) {
        this.class_id = class_id;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment t_test.
     */
    // TODO: Rename and change types and number of parameters
    public static t_test newInstance(String param1, String param2) {
        t_test fragment = new t_test();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exerciseItems = new ArrayList<ExerciseItem>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_t_test, container, false);
        rcv = view.findViewById(R.id.t_exercise_rcv);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final TextView pageTitle = view.findViewById(R.id.t_classing_title);
        queue = Volley.newRequestQueue(getContext());
        exerciseAdapter = new ExerciseAdapter(getContext(), exerciseItems, "CLASS_MODE");
        rcv = view.findViewById(R.id.t_exercise_rcv);
        rcv.addItemDecoration(new MyDecoration());
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv.setAdapter(exerciseAdapter);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1) {
                    try{
                        JSONObject response = (JSONObject)msg.obj;
                        int response_num = response.getInt("exercise_num");
                        if(response_num != receivedNum && response_num != 0) {
                            receivedNum = response_num;
                            pageTitle.setText("共接收到" + receivedNum + "道习题");
                            JSONArray items = response.getJSONArray("data");
                            exerciseItems.clear();
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
                            exerciseAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        handler.postDelayed(task, 1000);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if(!pptPushed) {
                getClassExercise(class_id, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        try {
                            JSONObject response = new JSONObject(result);
                            int code = response.getInt("code");
                            if(code == 0) {
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = response;
                                handler.sendMessage(msg);
                                System.out.println("接收到了习题");
                            } else if(code == 1) {
                                ToastUtil.showMsg(getContext(), "获取失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
                handlerDelay.postDelayed(this, 1000);
            }
        }
    };



    private void getClassExercise(int class_id, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/api/get_class_exercises/";
        //System.out.println(url);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("class_id", class_id);//测试
            //System.out.println(ppt_id);
            //jsonBody.put("course_code", course_code);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    callback.onSuccessResponse(jsonObject.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("onErrorResponse", volleyError.toString());
                    Toast.makeText(getContext(), "Error: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, 0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(handler != null) {
            handler.removeCallbacks(task);
            //System.out.println("暂停是空的");
        }
        if(handlerDelay != null) {
            handlerDelay.removeCallbacks(task);
        }
        //System.out.println("取消了");
    }

    @Override
    public void onStop() {

        super.onStop();
        if(handler != null) {
            handler.removeCallbacks(task);
            //System.out.println("结束不是空的");
        }
        if(handlerDelay != null) {
            handlerDelay.removeCallbacks(task);
        }
        //System.out.println("结束了");
    }


    @Override
    public void onResume() {

        super.onResume();
        if(handler!=null)
            handler.postDelayed(task, 1000);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if(handler!=null)
                handler.postDelayed(task, 1000);
            //System.out.println("--------Fragment 重新显示到最前端" );
        } else {
            if(handler != null)
                handler.removeCallbacks(task);
            if(handlerDelay != null) {
                handlerDelay.removeCallbacks(task);
            }
            //System.out.println("--------Fragment weixianshi");
        }
    }


    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }
}