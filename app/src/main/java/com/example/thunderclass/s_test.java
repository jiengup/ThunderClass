package com.example.thunderclass;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link s_test#newInstance} factory method to
 * create an instance of this fragment.
 */
public class s_test extends Fragment {

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
    public s_test() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment s_test.
     */
    // TODO: Rename and change types and number of parameters
    public static s_test newInstance(String param1, String param2) {
        s_test fragment = new s_test();
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
        return inflater.inflate(R.layout.fragment_s_test, container, false);
    }
}