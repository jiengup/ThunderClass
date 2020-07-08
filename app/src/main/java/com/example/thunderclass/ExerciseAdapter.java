package com.example.thunderclass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.LinearViewHolder>{

    private Context context;
    private ArrayList<ExerciseItem> list;
    private OnItemClickListener listener;
    private String task;

    private OnItemClickListener onItemClickListener = null;
    public ExerciseAdapter(Context context, ArrayList<ExerciseItem> list, String task) {
        this.context = context;
        this.list = list;
        this.task = task;
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.exercise_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, final int position) {
        final ExerciseItem exerciseItem = list.get(position);
        int type = exerciseItem.getType();
        if(type == 0) {
            holder.type.setText("简答题");
            holder.exercisePubtime.setText(exerciseItem.getPubTime());
            holder.rb_A.setVisibility(View.INVISIBLE);
            holder.rb_B.setVisibility(View.INVISIBLE);
            holder.rb_C.setVisibility(View.INVISIBLE);
            holder.rb_D.setVisibility(View.INVISIBLE);
            holder.content.setText(exerciseItem.getContent());
        } else{
            holder.type.setText("选择题");
            holder.exercisePubtime.setText(exerciseItem.getPubTime());
            int choice_num = exerciseItem.getChoice_num();
            if(task != null && !task.equals("CLASS_MODE")){
                holder.rb_A.setEnabled(false);
                holder.rb_B.setEnabled(false);
                holder.rb_C.setEnabled(false);
                holder.rb_D.setEnabled(false);
            }
            holder.content.setText(exerciseItem.getContent());
            switch (choice_num) {
                case 3:
                    holder.rb_D.setVisibility(View.INVISIBLE);
                    holder.rb_A.setText(exerciseItem.getChoiceA());
                    holder.rb_B.setText(exerciseItem.getChoiceB());
                    holder.rb_C.setText(exerciseItem.getChoiceC());
                    break;
                case 2:
                    holder.rb_C.setVisibility(View.INVISIBLE);
                    holder.rb_D.setVisibility(View.INVISIBLE);
                    holder.rb_A.setText(exerciseItem.getChoiceA());
                    holder.rb_B.setText(exerciseItem.getChoiceB());
                    break;
                default:
                    holder.rb_A.setVisibility(View.VISIBLE);
                    holder.rb_B.setVisibility(View.VISIBLE);
                    holder.rb_C.setVisibility(View.VISIBLE);
                    holder.rb_D.setVisibility(View.VISIBLE);
                    holder.rb_A.setText(exerciseItem.getChoiceA());
                    holder.rb_B.setText(exerciseItem.getChoiceB());
                    holder.rb_C.setText(exerciseItem.getChoiceC());
                    holder.rb_D.setText(exerciseItem.getChoiceD());
            }
            int correct = exerciseItem.getCorrectAns();
            if(task != null && !task.equals("CLASS_MODE")) {
                switch(correct) {
                    case 1:
                        holder.rb_A.setChecked(true);
                        holder.rb_A.setTextColor(Color.RED);
                        break;
                    case 2:
                        holder.rb_B.setChecked(true);
                        holder.rb_B.setTextColor(Color.RED);
                        break;
                    case 3:
                        holder.rb_C.setChecked(true);
                        holder.rb_C.setTextColor(Color.RED);
                        break;
                    case 4:
                        holder.rb_D.setChecked(true);
                        holder.rb_D.setTextColor(Color.RED);
                        break;
                    default:
                        break;
                }
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
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



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private RadioButton rb_A, rb_B, rb_C, rb_D;
        private TextView content;
        private TextView exercisePubtime;
        private TextView type;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.exercise_type);
            rb_A = itemView.findViewById(R.id.rb_A);
            rb_B = itemView.findViewById(R.id.rb_B);
            rb_C = itemView.findViewById(R.id.rb_C);
            rb_D = itemView.findViewById(R.id.rb_D);
            exercisePubtime = itemView.findViewById(R.id.exercise_time);
            content = itemView.findViewById(R.id.exercise_content);
        }
    }
}
