package com.github.f4irline.taskscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Goals.GoalsActivity;
import com.github.f4irline.taskscheduler.Tasks.TasksActivity;

import java.util.List;

public class MainActivity extends BaseActivity {

    public static boolean mainActive = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActive = true;

        ProgressBar progressBar = findViewById(R.id.progressBar);

        viewModel.getAllGoals().observe(this, goals -> {
            float totalGoalTime = 0;
            float totalGoalDone = 0;
            for (Goal goal : goals) {
                totalGoalTime += goal.getGoalTime();
                totalGoalDone += goal.getGoalDone();
            }

            float goalsRatio = (totalGoalDone / totalGoalTime) * 100;


            progressBar.setProgress((int) goalsRatio, true);
        });
    }
}
