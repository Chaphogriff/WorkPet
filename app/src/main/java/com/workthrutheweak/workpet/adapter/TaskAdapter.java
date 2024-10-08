package com.workthrutheweak.workpet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.workthrutheweak.workpet.R;
import com.workthrutheweak.workpet.model.Task;

import java.util.List;


//Adapter for Task List Recycler View
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private Context context;
    private List<Task> dataset;

    public TaskAdapter(Context context, List<Task> dataset) {
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
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Task item = dataset.get(position); //changer pour FF
        List<String> TaskString = item.inString();
        holder.getTaskTitleTextView().setText(TaskString.get(0));
        holder.getTaskDescTextView().setText(TaskString.get(1));
        holder.getTaskDateTextView().setText(TaskString.get(2));
        holder.getTaskRewardTextView().setText(TaskString.get(3));
        holder.getTaskCompletionCheckBox().setChecked(item.isTaskDone());
        holder.getTaskCompletionCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setTaskDone(isChecked); //changer pour FF
            }
        });
        //delete Task
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int p = position;
                p = holder.getAdapterPosition();// obtenir la position actuelle de l'élément dans la liste
                CharSequence[] delete = {
                        "Delete",
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                int finalP = p;
                alert.setItems(delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            dataset.remove(finalP);// supprimer l'élément à la position actuelle
                            notifyItemRemoved(finalP);

                        }
                    }
                });
                alert.create().show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dataset != null) {
            return dataset.size();
        } else {
            return 0;
        }
    }
}
