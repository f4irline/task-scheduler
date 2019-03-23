package com.github.f4irline.taskscheduler.Tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.f4irline.taskscheduler.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskText;

        public ViewHolder(View itemView) {
            super(itemView);
            taskText = (TextView) itemView.findViewById(R.id.task_name);
        }
    }

    private List<Task> tasks;

    public TasksAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View scoresView = inflater.inflate(R.layout.item_task, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(scoresView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TasksAdapter.ViewHolder viewHolder, int i) {
        if (tasks != null) {
            Task task = tasks.get(i);
            viewHolder.taskText.setText(task.task);
        } else {
            viewHolder.taskText.setText("No tasks yet");
        }

    }

    void setTasks(List<Task> words){
        tasks = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (tasks != null) {
            return tasks.size();
        } else {
            return 0;
        }
    }
}
