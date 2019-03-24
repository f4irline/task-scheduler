package com.github.f4irline.taskscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.f4irline.taskscheduler.Goals.GoalsActivity;
import com.github.f4irline.taskscheduler.Tasks.TasksActivity;

public class MainActivity extends BaseActivity {

    public static boolean mainActive = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActive = true;
    }
}
