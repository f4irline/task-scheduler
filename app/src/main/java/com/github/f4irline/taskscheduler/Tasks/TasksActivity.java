package com.github.f4irline.taskscheduler.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.f4irline.taskscheduler.AppViewModel;
import com.github.f4irline.taskscheduler.BaseActivity;
import com.github.f4irline.taskscheduler.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TasksActivity extends BaseActivity {

    public static boolean tasksActive = false;

    List<Task> tasks;
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;

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

    public void onTaskAdd(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TASK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Task task = new Task(data.getStringExtra("task"));
            viewModel.insert(task);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Empty field",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void addTask(View v) {
        EditText taskField = findViewById(R.id.taskText);

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
