package com.example.thunderclass;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link s_ppt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class s_ppt extends Fragment {

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
    private List<String> pages;
    private PPTDetailAdapter pptDetailAdapter;
    private ListView listView;
    private RequestQueue queue;

    public s_ppt() {
        // Required empty public constructor
    }

    public s_ppt(int class_id) {
        this.class_id = class_id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment s_ppt.
     */
    // TODO: Rename and change types and number of parameters
    public static s_ppt newInstance(String param1, String param2) {
        s_ppt fragment = new s_ppt();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_s_ppt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView pageTitle = view.findViewById(R.id.t_ppt_message);
        queue = Volley.newRequestQueue(getContext());
        listView = view.findViewById(R.id.t_ppt_detail_list);
        pptDetailAdapter = new PPTDetailAdapter(getContext(), R.layout.ppt_card_item, pages);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1) {
                    pptPushed = true;
                    try {
                        JSONObject response = (JSONObject)msg.obj;
                        pageTitle.setText(response.getString("ppt_name"));
                        JSONArray items = response.getJSONArray("data");
                        for(int i=0; i<items.length(); i++) {
                            JSONObject item = (JSONObject)items.get(i);
                            pages.add("http://10.0.2.2:8000" + item.getString("page_url"));
                        }
                        listView.setAdapter(pptDetailAdapter);
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
                getClassPPT(class_id, new t_ppt.VolleyCallback() {
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
                                System.out.println("接收到了ppt");
                            } else if(code == 2) {
                                ToastUtil.showMsg(getContext(), "获取失败");
                            } else if(code == 1) {
                                System.out.println("还未发布PPT");
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


    private void getClassPPT(int class_id, final t_ppt.VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/api/get_class_ppt/";
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