package com.github.f4irline.taskscheduler.Tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.f4irline.taskscheduler.R;

import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskText;
        public ImageButton removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            taskText = (TextView) itemView.findViewById(R.id.task_name);
            removeButton = (ImageButton) itemView.findViewById(R.id.remove_task);
        }
    }

    private List<Task> tasks;
    private TaskViewModel taskViewModel;

    public TasksAdapter(List<Task> tasks, TaskViewModel taskViewModel) {
        this.tasks = tasks;
        this.taskViewModel = taskViewModel;
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
            final Task task = tasks.get(i);
            viewHolder.taskText.setText(task.task);
            viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    taskViewModel.delete(task);
               }
            });
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
