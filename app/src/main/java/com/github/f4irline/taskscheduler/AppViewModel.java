package com.github.f4irline.taskscheduler;

import android.app.Application;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Goals.GoalRepository;
import com.github.f4irline.taskscheduler.Tasks.Task;
import com.github.f4irline.taskscheduler.Tasks.TaskRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class AppViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;

    private GoalRepository goalRepository;
    private LiveData<List<Goal>> allGoals;

    public AppViewModel (Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        goalRepository = new GoalRepository(application);
        allTasks = taskRepository.getAllTasks();
        allGoals = goalRepository.getAllGoals();
    }

    public LiveData<List<Task>> getAllTasks() { return allTasks; }

    public LiveData<Boolean> checkIfTaskExists(String taskName) { return taskRepository.checkIfTaskExists(taskName); }

    public void insert(Task word) { taskRepository.insert(word); }

    public void delete(Task task) { taskRepository.delete(task); }

    public LiveData<List<Goal>> getAllGoals() { return allGoals; }

    public LiveData<Boolean> checkIfGoalExists(String goalName) { return goalRepository.checkIfGoalExists(goalName); }

    public void getTotalGoalTime() { goalRepository.getTotalGoalTime(); }

    public void insert(Goal goal) { goalRepository.insert(goal); }

    public void delete(Goal goal) { goalRepository.delete(goal); }

    public void update(int gId, float goalDone) {
        goalRepository.update(gId, goalDone);
    }

    public void updateGoalTotal(int gId, float goalDoneTotal) {
        goalRepository.updateGoalTotal(gId, goalDoneTotal);
    }

    public void goalTimeTick(int gId) {
        goalRepository.goalTimeTick(gId);
    }
}
