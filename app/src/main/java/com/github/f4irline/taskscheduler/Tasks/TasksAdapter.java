package com.github.f4irline.taskscheduler.Tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.f4irline.taskscheduler.AppViewModel;
import com.github.f4irline.taskscheduler.R;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskText;
        public ImageButton removeButton;
        private LinearLayout taskWrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            taskText = (TextView) itemView.findViewById(R.id.task_name);
            removeButton = (ImageButton) itemView.findViewById(R.id.remove_task);
            taskWrapper = (LinearLayout) itemView.findViewById(R.id.task_item_wrapper);
        }
    }

    private List<Task> tasks;
    private AppViewModel viewModel;
    private Animation deleteAnimation;
    private Context context;

    public TasksAdapter(List<Task> tasks, AppViewModel viewModel) {
        this.tasks = tasks;
        this.viewModel = viewModel;
    }

    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        deleteAnimation = AnimationUtils.loadAnimation(context, R.anim.scale);
        View scoresView = inflater.inflate(R.layout.item_task, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(scoresView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TasksAdapter.ViewHolder viewHolder, int i) {
        if (tasks != null) {
            final Task task = tasks.get(i);
            viewHolder.taskText.setText(task.task);
            viewHolder.removeButton.setOnClickListener(v -> {
                viewHolder.taskWrapper.startAnimation(deleteAnimation);
                deleteAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animation.setFillAfter(true);
                        viewModel.delete(task);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
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
