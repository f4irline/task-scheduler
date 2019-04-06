package com.github.f4irline.taskscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Goals.GoalsActivity;
import com.github.f4irline.taskscheduler.Tasks.TasksActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    public static boolean mainActive = true;
    private PieChart pieChart;
    private PieDataSet set;
    List<PieEntry> entries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActive = true;
        entries = new ArrayList<>();

        ProgressBar progressBar = findViewById(R.id.progressBar);

        viewModel.getAllGoals().observe(this, goals -> {
            float totalGoalTime = 0;
            float totalGoalDone = 0;
            List<PieEntry> goalEntries = new ArrayList<>();
            for (Goal goal : goals) {
                totalGoalTime += goal.getGoalTime();
                totalGoalDone += goal.getGoalDone();
                goalEntries.add(new PieEntry(goal.getGoalTime(), goal.getGoal()));
            }
            entries = goalEntries;

            float goalsRatio = (totalGoalDone / totalGoalTime) * 100;
            progressBar.setProgress((int) goalsRatio, true);

            initPieChart();
        });
    }

    private void initPieChart() {
        pieChart = findViewById(R.id.generalChart);
        pieChart.setTouchEnabled(false);

        set = new PieDataSet(entries, "");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(set);
        pieChart.setData(data);

        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        pieChart.setNoDataText("Nothing here yet. Maybe add some goals?");
        pieChart.setNoDataTextColor(R.color.colorPrimary);
        pieChart.setCenterText("YOUR GOALS");
        pieChart.setCenterTextColor(R.color.colorPrimary);
        pieChart.invalidate();
    }
}
