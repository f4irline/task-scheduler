package com.github.f4irline.taskscheduler.Tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.f4irline.taskscheduler.AppViewModel;
import com.github.f4irline.taskscheduler.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The adapter which manages the task list in the TasksActivity.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    /**
     * Describes an item view and metadata about it's place in the RecyclerView.
     *
     * @author Tommi Lepola
     * @version 3.0
     * @since 2019.0323
     */
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

    /**
     * Constructor for the adapter. Initializes the tasks that currently exist
     * and the viewModel which is used to manage the database.
     *
     * @param tasks list of the tasks that currently already exist.
     * @param viewModel the viewModel implementation which is used to manage the database.
     */
    public TasksAdapter(List<Task> tasks, AppViewModel viewModel) {
        this.tasks = tasks;
        this.viewModel = viewModel;
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     *
     * @param viewGroup the ViewGroup into which the new view will be added.
     * @param viewType the type of the new view.
     * @return a new ViewHolder which holds a View of the given view type.
     */
    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        deleteAnimation = AnimationUtils.loadAnimation(context, R.anim.scale);
        View tasksView = inflater.inflate(R.layout.item_task, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(tasksView);

        return viewHolder;
    }

    /**
     * Called by the RecyclerView to display the data at the specified position.
     *
     * @param viewHolder the ViewHolder which should be updated to represent the contents of the item
     *                   at the given position in the data set.
     * @param position the position of the item in the Adapter's data set.
     */
    @Override
    public void onBindViewHolder(TasksAdapter.ViewHolder viewHolder, int position) {
        if (tasks != null) {
            final Task task = tasks.get(position);
            viewHolder.taskText.setText(task.task);
            viewHolder.removeButton.setOnClickListener(v -> {
                final LiveData<Boolean> goalExists = viewModel.checkIfGoalExists(task.getTask());
                goalExists.observe((AppCompatActivity) context, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean exists) {
                        if (exists) {
                            viewModel.deleteGoalByName(task.getTask());
                        }

                        viewHolder.taskWrapper.startAnimation(deleteAnimation);
                        goalExists.removeObserver(this);
                    }
                });

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

    /**
     * Sets given tasks to the tasks list and notifies any registered observers that
     * the data set has changed.
     *
     * @param tasks the tasks that exist when this function is called.
     */
    void setTasks(List<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the number of tasks currently in the list.
     */
    @Override
    public int getItemCount() {
        if (tasks != null) {
            return tasks.size();
        } else {
            return 0;
        }
    }
}
