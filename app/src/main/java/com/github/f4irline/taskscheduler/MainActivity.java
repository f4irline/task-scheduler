package com.github.f4irline.taskscheduler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Http.FactResponse;
import com.github.f4irline.taskscheduler.Http.HttpFetch;
import com.github.f4irline.taskscheduler.Http.TaskListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The starting point of the application. Acts as the dashboard for the user.
 *
 * <p>
 * The MainActivity is the first activity user sees when the user opens the app. MainActivity
 * holds a random fact fetched from an API using HTTP requests, progress bar which displays
 * user's total progress on all his goals and a doghnut chart which displays how user has
 * allocated his time to goals.
 * </p>
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
public class MainActivity extends BaseActivity implements TaskListener {

    public static boolean mainActive = true;
    private PieChart pieChart;
    private TextView randomFact;

    /**
     * Called when the activity is launched.
     *
     * <p>
     * Initializes the main activity by calling methods that initialize the pie chart, the random
     * fact and calculates progress for progress bar.
     * </p>
     * @param savedInstanceState activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActive = true;

        ProgressBar progressBar = findViewById(R.id.progressBar);

        initPieChart();

        viewModel.getAllGoals().observe(this, goals -> {
            float totalGoalTime = 0;
            float totalGoalDone = 0;
            List<PieEntry> goalEntries = new ArrayList<>();
            for (Goal goal : goals) {
                totalGoalTime += goal.getGoalTime();
                totalGoalDone += goal.getGoalDone();
                goalEntries.add(new PieEntry(goal.getGoalTime(), goal.getGoal()));
            }

            if (!goalEntries.isEmpty()) {
                PieDataSet set = new PieDataSet(goalEntries, "");
                set.setColors(ColorTemplate.MATERIAL_COLORS);

                PieData data = new PieData(set);
                pieChart.setData(data);
            } else {
                pieChart.setData(null);
            }

            float goalsRatio = (totalGoalDone / totalGoalTime) * 100;
            progressBar.setProgress((int) goalsRatio, true);
        });

        getRandomFact();
    }

    /**
     * Initializes the pie chart.
     */
    private void initPieChart() {
        pieChart = findViewById(R.id.generalChart);
        pieChart.setTouchEnabled(false);

        pieChart.setNoDataText("Nothing here yet. Maybe add some goals?");
        pieChart.setNoDataTextColor(R.color.black);
        pieChart.setCenterText("YOUR GOALS");

        Description desc = new Description();
        desc.setText("");

        pieChart.setDescription(desc);
        pieChart.getLegend().setEnabled(false);

        pieChart.invalidate();
    }

    /**
     * Initializes the random fact part of the activity.
     */
    private void getRandomFact() {
        randomFact = findViewById(R.id.randomFact);
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = mgr.getActiveNetworkInfo();

        if (network != null & network.isConnected()) {
            try {
                fetchFact("http://randomuselessfact.appspot.com/random.json?language=en");
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Uses HttpFetch class to fetch the random fact.
     *
     * @param url the url where the fact is fetched from.
     * @throws MalformedURLException if URL is malformed like incorrect protocol or such.
     */
    private void fetchFact(String url) throws MalformedURLException {
        new HttpFetch(this).execute(new URL(url));
    }

    /**
     * Parses the random fact from the http get request after the request is done.
     *
     * @param result the response from the http get request.
     */
    @Override
    public void onTaskCompleted(String result) {
        Gson gson = new Gson();
        FactResponse response = gson.fromJson(result, FactResponse.class);
        randomFact.setText(response.getText());
    }
}
