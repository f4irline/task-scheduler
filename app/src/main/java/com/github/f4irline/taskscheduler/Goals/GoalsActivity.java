package com.github.f4irline.taskscheduler.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.f4irline.taskscheduler.BaseActivity;
import com.github.f4irline.taskscheduler.R;
import com.github.f4irline.taskscheduler.Tasks.Task;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The goals activity.
 *
 * <p>
 * Holds controls to add and delete goals. Also holds a list of all goals user has added.
 * </p>
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
public class GoalsActivity extends BaseActivity {

    public static boolean goalsActive = false;

    List<Goal> goals;
    public static final int NEW_GOAL_ACTIVITY_REQUEST_CODE = 1;

    private Spinner goalsSpinner;

    /**
     * Called when the activity is created.
     *
     * <p>
     * Initializes the list of the goals and observes changes.
     * </p>
     *
     * @param savedInstanceState the previously saved state of the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        goalsSpinner = (Spinner) findViewById(R.id.goals_spinner);

        RecyclerView goalsList = (RecyclerView) findViewById(R.id.goal_list);
        goalsList.setLayoutManager(new LinearLayoutManager(this));

        final GoalsAdapter goalsAdapter = new GoalsAdapter(goals, viewModel, getApplicationContext());
        goalsList.setAdapter(goalsAdapter);

        viewModel.getAllGoals().observe(this, goals -> {
            goalsAdapter.setGoals(goals);
        });

        viewModel.getAllTasks().observe(this, tasks -> {
            ArrayAdapter<Task> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, tasks);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            goalsSpinner.setAdapter(adapter);
        });

        goalsActive = true;
    }

    /**
     * Handles adding a new goal.
     *
     * @param requestCode the request code reference to the add action.
     * @param resultCode the implication if the add task is OK to be executed.
     * @param data the data in a intent.
     */
    public void onGoalAdd(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_GOAL_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Goal goal = new Goal(data.getStringExtra("goal"), data.getFloatExtra("time", 0));

            final LiveData<Boolean> goalExists = viewModel.checkIfGoalExists(goal.getGoal());
            goalExists.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean exists) {
                    if (!exists) {
                        viewModel.insert(goal);
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Goal with this task already exists.",
                                Toast.LENGTH_LONG).show();
                    }
                    goalExists.removeObserver(this);
                }
            });
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Time must be a number.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when user clicks add for a goal.
     *
     * <p>
     * Validates that the goal time field is not empty, then creates new intent which holds the
     * name of the goal and the time and calls the goal add method to finalize the insert of a new goal.
     * </p>
     *
     * <p>
     * If the goal time field is empty, then the onTaskAdd method is called with RESULT_CANCELED
     * value, and user is told that there is an empty field which needs to be filled.
     * </p>
     *
     * @param v the add button which is clicked.
     */
    public void addGoal(View v) {
        EditText timeField = findViewById(R.id.timeText);

        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(timeField.getText()) || TextUtils.isEmpty(goalsSpinner.getSelectedItem().toString())
            || !isValidNumber(timeField.getText().toString())) {
            onGoalAdd(NEW_GOAL_ACTIVITY_REQUEST_CODE, RESULT_CANCELED, replyIntent);
        } else {
            replyIntent.putExtra("time", Float.parseFloat(timeField.getText().toString()));
            replyIntent.putExtra("goal", goalsSpinner.getSelectedItem().toString());
            setResult(RESULT_OK, replyIntent);
            onGoalAdd(NEW_GOAL_ACTIVITY_REQUEST_CODE, RESULT_OK, replyIntent);
        }

        timeField.setText("");
    }

    /**
     * Used to check that user has given a valid time value to the time text field.
     *
     * @param input the user's input
     * @return false if the input is not valid, true if it is.
     */
    private boolean isValidNumber(String input) {
        try {
            Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
}
