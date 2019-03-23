package com.github.f4irline.taskscheduler.Goals;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GoalViewModel extends AndroidViewModel {
    private GoalRepository goalRepository;
    private LiveData<List<Goal>> allGoals;

    public GoalViewModel (Application application) {
        super(application);
        goalRepository = new GoalRepository(application);
        allGoals = goalRepository.getAllGoals();
    }

    LiveData<List<Goal>> getAllGoals() { return allGoals; }

    public void insert(Goal word) { goalRepository.insert(word); }
}
