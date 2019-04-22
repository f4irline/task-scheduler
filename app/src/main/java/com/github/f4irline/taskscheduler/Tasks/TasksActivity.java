package com.github.f4irline.taskscheduler.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.f4irline.taskscheduler.BaseActivity;
import com.github.f4irline.taskscheduler.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The tasks activity.
 *
 * <p>
 * Holds controls to add and delete tasks. Also holds a list of all tasks user has added.
 * </p>
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
public class TasksActivity extends BaseActivity {

    public static boolean tasksActive = false;

    List<Task> tasks;
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;

    /**
     * Called when the activity is created.
     *
     * <p>
     * Initializes the list of the tasks and observes for changes.
     * </p>
     *
     * @param savedInstanceState the previously saved state of the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        RecyclerView tasksList = (RecyclerView) findViewById(R.id.task_list);
        tasksList.setLayoutManager(new LinearLayoutManager(this));

        final TasksAdapter tasksAdapter = new TasksAdapter(tasks, viewModel);
        tasksList.setAdapter(tasksAdapter);

        viewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> words) {
                tasksAdapter.setTasks(words);
            }
        });
        tasksActive = true;
    }

    /**
     * Handles adding a new task.
     *
     * @param requestCode the request code reference to the add action.
     * @param resultCode the implication if the add task is OK to be executed.
     * @param data the data in a intent.
     */
    public void onTaskAdd(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TASK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Task task = new Task(data.getStringExtra("task"));

            final LiveData<Boolean> taskExists = viewModel.checkIfTaskExists(task.getTask());

            taskExists.observe(this, new Observer<Boolean>() {
                /**
                 * Observes the LiveData of the tasks and removes the observer after
                 * the data has been fetched, since we only need to observe this data once.
                 *
                 * <p>
                 * Used to check if a task with the given name already exists.
                 * </p>
                 * @param exists true if a task with the given name already exists, false if not.
                 */
                @Override
                public void onChanged(@Nullable Boolean exists) {
                    if (!exists) {
                        viewModel.insert(task);
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Task with this name already exists.",
                                Toast.LENGTH_LONG).show();
                    }
                    taskExists.removeObserver(this);
                }
            });
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Empty field",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when user clicks add for a task.
     *
     * <p>
     * Validates that the task name field is not empty, then creates new intent which holds the
     * name of the task and calls the task add method to finalize the insert of a new task.
     * </p>
     *
     * <p>
     * If the task name field is empty, then the onTaskAdd method is called with RESULT_CANCELED
     * value, and user is told that there is an empty field which needs to be filled.
     * </p>
     *
     * @param v the add button which is clicked.
     */
    public void addTask(View v) {
        EditText taskField = findViewById(R.id.taskTextInput);

        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(taskField.getText())) {
            onTaskAdd(NEW_TASK_ACTIVITY_REQUEST_CODE, RESULT_CANCELED, replyIntent);
        } else {
            replyIntent.putExtra("task", taskField.getText().toString());
            setResult(RESULT_OK, replyIntent);
            onTaskAdd(NEW_TASK_ACTIVITY_REQUEST_CODE, RESULT_OK, replyIntent);
        }

        taskField.setText("");
    }
}
