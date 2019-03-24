package com.github.f4irline.taskscheduler.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.f4irline.taskscheduler.AppViewModel;
import com.github.f4irline.taskscheduler.BaseActivity;
import com.github.f4irline.taskscheduler.R;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GoalsActivity extends BaseActivity {

    public static boolean goalsActive = false;

    List<Goal> goals;
    public static final int NEW_GOAL_ACTIVITY_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        RecyclerView goalsList = (RecyclerView) findViewById(R.id.goal_list);
        goalsList.setLayoutManager(new LinearLayoutManager(this));

        final GoalsAdapter goalsAdapter = new GoalsAdapter(goals, viewModel);
        goalsList.setAdapter(goalsAdapter);

        viewModel.getAllGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(@Nullable final List<Goal> words) {
                // Update the cached copy of the words in the adapter.
                goalsAdapter.setGoals(words);
            }
        });
        goalsActive = true;
    }

    public void onGoalAdd(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_GOAL_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Goal goal = new Goal(data.getStringExtra("goal"), data.getFloatExtra("time", 0));
            viewModel.insert(goal);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Empty field",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void addGoal(View v) {
        EditText goalField = findViewById(R.id.goalText);
        EditText timeField = findViewById(R.id.timeText);

        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(goalField.getText()) || TextUtils.isEmpty(timeField.getText())) {
            onGoalAdd(NEW_GOAL_ACTIVITY_REQUEST_CODE, RESULT_CANCELED, replyIntent);
        } else {
            replyIntent.putExtra("goal", goalField.getText().toString());
            replyIntent.putExtra("time", Float.parseFloat(timeField.getText().toString()));
            setResult(RESULT_OK, replyIntent);
            onGoalAdd(NEW_GOAL_ACTIVITY_REQUEST_CODE, RESULT_OK, replyIntent);
        }

        goalField.setText("");
        timeField.setText("");
    }
}
