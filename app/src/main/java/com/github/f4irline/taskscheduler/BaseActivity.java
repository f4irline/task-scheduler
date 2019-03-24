package com.github.f4irline.taskscheduler;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.github.f4irline.taskscheduler.Goals.GoalsActivity;
import com.github.f4irline.taskscheduler.Tasks.TasksActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
    public NavigationView navigationView;
    public FloatingActionButton floatingActionButton;
    public Context mContext;

    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        setContentView(R.layout.activity_base);
    }

    @Override
    public void setContentView(final int layoutResId) {
        DrawerLayout drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) drawerLayout.findViewById(R.id.layout_container);
        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(drawerLayout);

        initToolbar();

        floatingActionButton = drawerLayout.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("setContentView()", "Clicked floating button");
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                }
                else {
                    menuItem.setChecked(true);
                }

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.nav_dash:
                        if (!MainActivity.mainActive) {
                            GoalsActivity.goalsActive = false;
                            TasksActivity.tasksActive = false;
                            Intent dashIntent = new Intent(mContext, MainActivity.class);
                            startActivity(dashIntent);
                            return true;
                        }
                        break;
                    case R.id.nav_tasks:
                        if (!TasksActivity.tasksActive) {
                            MainActivity.mainActive = false;
                            GoalsActivity.goalsActive = false;
                            Intent tasksIntent = new Intent(mContext, TasksActivity.class);
                            startActivity(tasksIntent);
                            return true;
                        }
                        break;
                    case R.id.nav_goals:
                        if (!GoalsActivity.goalsActive) {
                            MainActivity.mainActive = false;
                            TasksActivity.tasksActive = false;
                            Intent goalsIntent = new Intent(mContext, GoalsActivity.class);
                            startActivity(goalsIntent);
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        drawerToggle.syncState();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNav();
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
