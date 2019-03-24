package com.github.f4irline.taskscheduler;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ImageButton backButton;
    TextView screenTitle;

    @Override
    public void setContentView(final int layoutResId) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) coordinatorLayout.findViewById(R.id.layout_container);
        backButton = coordinatorLayout.findViewById(R.id.image_back_button);
        screenTitle = coordinatorLayout.findViewById(R.id.text_screen_title);

        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(coordinatorLayout);
    }

    public void setScreenTitle(String title) {
        this.screenTitle.setText(title);
    }

    public ImageButton getBackButton() {
        return this.backButton;
    }
}
