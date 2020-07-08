package com.example.thunderclass;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.thunderclass.PPTDetailAdapter;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PPTDetailActivity extends AppCompatActivity {

    private int pptId;
    private String pptName;
    private ListView listView;
    private TextView ppt_name;
    private PPTDetailAdapter pptDetailAdapter;
    private List<String> pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_p_t_detail);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        pptId = intent.getIntExtra("ppt_id", 10);
        pptName = intent.getStringExtra("ppt_name");

        ppt_name = findViewById(R.id.ppt_detail_name);
        ppt_name.setText(pptName);
        pages = new ArrayList<String>();
        listView = findViewById(R.id.ppt_detail_list);
        pptDetailAdapter = new PPTDetailAdapter(this, R.layout.ppt_card_item, pages);
        getPPTList(pptId, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try{
                    //TODO：这里有ppt排序不正确的情况
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if(code == 0) {
                        JSONArray items = jsonObject.getJSONArray("data");
                        for(int i=0; i<items.length(); i++) {
                            JSONObject item = (JSONObject)items.get(i);
                            pages.add("http://10.0.2.2:8000" + item.getString("page_url"));
                        }
                        listView.setAdapter(pptDetailAdapter);
                    } else {
                        ToastUtil.showMsg(getApplicationContext(), "获取失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void getPPTList(int ppt_id, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/api/get_ppt_detail/";
        System.out.println(url);
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("ppt_id", ppt_id);//测试
            System.out.println(ppt_id);
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
                    Toast.makeText(getApplicationContext(), "Error: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
