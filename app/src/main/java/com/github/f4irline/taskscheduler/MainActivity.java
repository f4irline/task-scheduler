package com.github.f4irline.taskscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.f4irline.taskscheduler.Goals.GoalsActivity;
import com.github.f4irline.taskscheduler.Tasks.TasksActivity;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void navClicked(View v) {
        switch(v.getId()) {
            case R.id.goalsNav:
                Intent goalsIntent = new Intent(this, GoalsActivity.class);
                startActivity(goalsIntent);
                break;
            case R.id.tasksNav:
                Intent tasksIntent = new Intent(this, TasksActivity.class);
                startActivity(tasksIntent);
                break;
        }
    }
}
