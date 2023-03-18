package com.workthrutheweak.workpet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.workthrutheweak.workpet.R;
import com.workthrutheweak.workpet.model.Task;

import java.util.List;
import java.util.StringJoiner;


//Adapter for Task List Recycler View
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private Context context;
    private List<Task> dataset;

    public TaskAdapter(Context context, List<Task> dataset){
        this.context = context;
        this.dataset = dataset;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView taskTitleTextView;
        private TextView taskDescTextView;
        private TextView taskDateTextView;
        private TextView taskRewardTextView;
        private CheckBox taskCompletionCheckBox;

        private Context Context;

        public ViewHolder(View view) {
            super(view);
            taskTitleTextView = view.findViewById(R.id.task_title);
            taskDescTextView = view.findViewById(R.id.task_desc);
            taskDateTextView = view.findViewById(R.id.task_date);
            taskRewardTextView = view.findViewById(R.id.task_reward);
            taskCompletionCheckBox = view.findViewById(R.id.task_completion);

        }

        public TextView getTaskTitleTextView() {
            return taskTitleTextView;
        }

        public TextView getTaskDescTextView() {
            return taskDescTextView;
        }

        public TextView getTaskDateTextView() {
            return taskDateTextView;
        }

        public TextView getTaskRewardTextView() {
            return taskRewardTextView;
        }

        public CheckBox getTaskCompletionCheckBox() {
            return taskCompletionCheckBox;
        }
    }


    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        Task item = dataset.get(position);
        holder.getTaskTitleTextView().setText(context.getResources().getString(item.titleId));
        holder.getTaskDescTextView().setText(context.getResources().getString(item.descriptionId));
        holder.getTaskDateTextView().setText(context.getResources().getString(item.dateId));
        holder.getTaskRewardTextView().setText(context.getResources().getString(item.rewardId));
        holder.getTaskCompletionCheckBox().setChecked(item.isTaskDone);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
