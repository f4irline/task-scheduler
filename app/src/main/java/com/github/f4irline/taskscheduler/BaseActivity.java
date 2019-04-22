package com.github.f4irline.taskscheduler;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.github.f4irline.taskscheduler.Goals.GoalsActivity;
import com.github.f4irline.taskscheduler.Tasks.TasksActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

/**
 * The base activity of the app.
 *
 * <p>
 * Acts as the base activity which every other activity inherits. Basically holds the navigation
 * drawer and floating action button.
 * </p>
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
    public NavigationView navigationView;
    public Context mContext;

    protected AppViewModel viewModel;

    /**
     * Returns the navigation view.
     *
     * @return the navigation view.
     */
    public NavigationView getNavigationView() {
        return navigationView;
    }

    /**
     * Called when the base activity is created.
     *
     * <p>
     * Initializes the activity by saving the context where the base activity is used and
     * initializes the viewModel which is used to interact with the database.
     * </p>
     *
     * @param savedInstanceState the previously saved state of the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        setContentView(R.layout.activity_base);

        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
    }

    /**
     * Initializes the view where the base activity is used, by inflating the base layout from
     * a layout resource.
     *
     * @param layoutResId the resource id to be inflated.
     */
    @Override
    public void setContentView(final int layoutResId) {
        DrawerLayout drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) drawerLayout.findViewById(R.id.layout_container);
        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(drawerLayout);

        initToolbar();
    }

    /**
     * Initializes the toolbar which holds the button to open the navigation drawer.
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Initializes the navigation drawer.
     */
    private void setUpNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            /**
             * Called when a navigation item is selected.
             *
             * @param menuItem the item which was selected.
             * @return true if a menuItem with some functionality was clicked.
             */
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

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

        setNavigationViewCheckedItem();
        drawerToggle.syncState();
    }

    /**
     * Handles setting the last clicked navigation item checked to display the selected
     * item in the navigation drawer properly.
     */
    private void setNavigationViewCheckedItem() {
        if (this.getClass().equals(MainActivity.class)) {
            navigationView.setCheckedItem(R.id.nav_dash);
        } else if (this.getClass().equals(GoalsActivity.class)) {
            navigationView.setCheckedItem(R.id.nav_goals);
        } else {
            navigationView.setCheckedItem(R.id.nav_tasks);
        }
    }

    /**
     * Called when the activity start up is complete. Initializes the navigation drawer
     * and synchronizes the state of the drawer indicator with the DrawerLayout.
     *
     * @param savedInstanceState the previous state of the activity.
     */
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNav();
        drawerToggle.syncState();
    }

    /**
     * Called by the system when the configuration changes.
     *
     * @param newConfig the new configuration which is synced to the drawer indicator.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Called when anything in anything in the app bar (so basically the drawer button) is clicked.
     *
     * @param item the item which was clicked.
     * @return true to consume the selection, false to proceed with the menu processing.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the floating action button was clicked.
     *
     * @param v the floating action button which was clicked.
     */
    public void floatingButtonClickedHandler(View v) {
        HoursDialogFragment dialog = new HoursDialogFragment();
        dialog.show(getSupportFragmentManager(), "goals");
    }
}
