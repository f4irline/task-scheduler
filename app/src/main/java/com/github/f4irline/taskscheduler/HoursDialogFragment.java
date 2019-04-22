package com.github.f4irline.taskscheduler;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.TimerTools.TimerReceiver;
import com.github.f4irline.taskscheduler.TimerTools.TimerReceiverCallback;
import com.github.f4irline.taskscheduler.TimerTools.TimerService;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * The dialog fragment which user operates when adding hours to goals.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0404
 */
public class HoursDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TimerReceiverCallback {
    private AppViewModel viewModel;

    private Spinner goalsSpinner;
    private Button saveButton;
    private Button timerButton;
    private TextView timerText;

    private EditText timeField;

    private List<Goal> goalsList;

    public static int chosenItem;

    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;

    /**
     * Called when the dialog is created.
     *
     * <p>
     * Initializes the dialog fragment.
     * </p>
     * @param savedInstanceState previously saved state of the dialog.
     * @return the created builder which is then used to show the dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_goal, null);

        builder.setView(view);
        builder.setTitle("Add progress on a goal");

        goalsSpinner = (Spinner) view.findViewById(R.id.dialog_goals_spinner);
        goalsSpinner.setEnabled(false);

        saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setEnabled(false);

        timerButton = (Button) view.findViewById(R.id.timerButton);
        timerButton.setEnabled(false);

        timeField = (EditText) view.findViewById(R.id.timeText);

        timerText = (TextView) view.findViewById(R.id.timerText);

        viewModel.getAllGoals().observe(this, goals -> {
            ArrayAdapter<Goal> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, goals);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if (goals.isEmpty()) {
                adapter.add(new Goal("Empty", 0));
            } else {
                goalsSpinner.setEnabled(true);
                timerButton.setEnabled(true);
                saveButton.setEnabled(true);
            }

            goalsList = goals;
            goalsSpinner.setAdapter(adapter);
        });

        saveButton.setOnClickListener(this);
        timerButton.setOnClickListener(this);
        goalsSpinner.setOnItemSelectedListener(this);

        if (TimerService.isRunning) {
            timerButton.setText("Stop timer");
        }

        TimerReceiver receiver = new TimerReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("android.intent.action.TIME_TICK"));

        return builder.create();
    }

    /**
     * Called when a button is clicked.
     *
     * @param v reference to the button which was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                saveGoalProgress();
                break;
            case R.id.timerButton:
                timerControl();
                break;
            default:
                break;
        }
    }

    /**
     * Saves the user's input for hours done for a goal.
     */
    private void saveGoalProgress() {
        if (TextUtils.isEmpty(timeField.getText())) {
            Toast.makeText(
                    this.getContext(),
                    "Empty field",
                    Toast.LENGTH_LONG).show();
        } else {
            viewModel.update(goalsList.get(chosenItem).goalId, Float.parseFloat(timeField.getText().toString()));
            this.dismiss();
        }
    }

    /**
     * Calls a method to start or stop the timer service.
     */
    private void timerControl() {
        if (TimerService.isRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    /**
     * Starts the timer service.
     */
    private void startTimer() {
        timerButton.setText("Stop timer");
        Intent intent = new Intent(this.getActivity().getApplicationContext(), TimerService.class);
        this.getContext().startService(intent);
    }

    /**
     * Stops the timer service.
     */
    private void stopTimer() {
        timerButton.setText("Start timer");
        Intent intent = new Intent(this.getActivity().getApplicationContext(), TimerService.class);
        this.getContext().stopService(intent);
    }

    /**
     * Saves the index of the chosen item on the dropdown list to a variable.
     *
     * @param parent the AdapterView where the selection happened.
     * @param view the view within the AdapterView which was clicked.
     * @param position the index of the clicked view in the AdapterView.
     * @param id the row id of the item that was selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenItem = position;
    }

    /**
     * Called when the AdapterView is initialized, selects the first item in the list.
     *
     * @param parent the AdapterView where the selection happened.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        chosenItem = 0;
    }

    /**
     * Callback function which is called on every time tick of the timer receiver.
     *
     * <p>
     * Updates the current time to the DialogFragment timerText TextField.
     * </p>
     *
     * @param intent the intent which called the callback.
     */
    @Override
    public void onTimeReceived(Intent intent) {
        Bundle extras = intent.getExtras();

        String secondsText;
        String minutesText;
        String hoursText;

        if (extras != null) {
            seconds = extras.getInt("seconds");
            minutes = extras.getInt("minutes");
            hours = extras.getInt("hours");
        }

        if (seconds < 10) {
            secondsText = "0"+seconds;
        } else {
            secondsText = String.valueOf(seconds);
        }

        if (minutes < 10) {
            minutesText = "0"+minutes;
        } else {
            minutesText = String.valueOf(minutes);
        }

        if (hours < 10) {
            hoursText = "0"+hours;
        } else {
            hoursText = String.valueOf(hours);
        }

        timerText.setText(hoursText+":"+minutesText+":"+secondsText);

        viewModel.goalTimeTick(goalsList.get(chosenItem).goalId);
    }
}
