package com.github.f4irline.taskscheduler.Tasks;

import android.os.Bundle;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Goals.GoalsAdapter;
import com.github.f4irline.taskscheduler.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TasksActivity extends AppCompatActivity {

    List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        readTasks();

        RecyclerView tasksList = (RecyclerView) findViewById(R.id.task_list);
        tasksList.setLayoutManager(new LinearLayoutManager(this));

        TasksAdapter tasksAdapter = new TasksAdapter(tasks);
        tasksList.setAdapter(tasksAdapter);
    }

    private void readTasks() {
        tasks = new ArrayList<>();

        Task codeTask = new Task();
        codeTask.task = "Code";

        Task readTask = new Task();
        readTask.task = "Read";

        tasks.add(codeTask);
        tasks.add(readTask);
    }
}
