package com.github.f4irline.taskscheduler;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Goals.GoalsActivity;
import com.github.f4irline.taskscheduler.Tasks.TasksActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

        getRandomFact();
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

    private void getRandomFact() {
        TextView randomFact = findViewById(R.id.randomFact);
        String fact = "";
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = mgr.getActiveNetworkInfo();

        if (network != null & network.isConnected()) {
            try {
                fact = fetchFact();
                Log.d("getRandomFact", fact);
            } catch (IOException ex) {
                Log.d("getRandomFact", "Error fetching fact.");
            }
        }
    }

    private String fetchFact() throws IOException {
        InputStream in = null;

        try {
            URL url = new URL("http://randomuselessfact.appspot.com/today.json?language=en");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();

            int myChar;
            String result = "";
            while ((myChar = in.read()) != -1) {
                result += (char) myChar;
            }

            return result;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
