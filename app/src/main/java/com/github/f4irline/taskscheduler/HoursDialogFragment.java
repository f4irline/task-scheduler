package com.github.f4irline.taskscheduler;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.f4irline.taskscheduler.Goals.Goal;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class HoursDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private AppViewModel viewModel;

    private Spinner goalsSpinner;
    private Button saveButton;
    private Button timerButton;

    private EditText timeField;

    private List<Goal> goalsList;

    private int chosenItem;

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
        goalsSpinner.setOnItemSelectedListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(timeField.getText())) {
            Toast.makeText(
                    this.getContext(),
                    "Empty field",
                    Toast.LENGTH_LONG).show();
        } else {
            viewModel.update(goalsList.get(chosenItem).goalId, Float.parseFloat(timeField.getText().toString()));
            Log.d("onClick", "Goal ID: "+goalsList.get(chosenItem).goalId+", Goal: "+goalsList.get(chosenItem).goal+", Time: "+timeField.getText());
            this.dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenItem = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        chosenItem = 0;
    }
}
