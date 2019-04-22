package com.github.f4irline.taskscheduler;

import android.app.Application;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Goals.GoalRepository;
import com.github.f4irline.taskscheduler.Tasks.Task;
import com.github.f4irline.taskscheduler.Tasks.TaskRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Class used for managing and using the tasks and goals database.
 *
 * <p>
 * Most of the data from database is served as a LiveData for the application to be able to react
 * and observe changes.
 * </p>
 *
 * <p>
 * LiveData is used to enforce "reactive programming" approach when managing and using the data.
 * </p>
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0324
 */
public class AppViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;

    private GoalRepository goalRepository;
    private LiveData<List<Goal>> allGoals;

    /**
     * Constructor for the view model.
     *
     * <p>
     * Initializes the repositories for use and fetches all tasks and goals currently defined
     * to the database.
     * </p>
     *
     * @param application the application reference which is used to initialize the repositories.
     */
    public AppViewModel (Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        goalRepository = new GoalRepository(application);
        allTasks = taskRepository.getAllTasks();
        allGoals = goalRepository.getAllGoals();
    }

    /**
     * Returns all tasks.
     *
     * @return all tasks.
     */
    public LiveData<List<Task>> getAllTasks() { return allTasks; }

    /**
     * Checks if task with a certain name already exists in the database.
     *
     * @param taskName the name of the task which is checked.
     * @return true if a task with the taskName exists, false if not.
     */
    public LiveData<Boolean> checkIfTaskExists(String taskName) { return taskRepository.checkIfTaskExists(taskName); }

    /**
     * Inserts a task to the database.
     *
     * @param task the task which is insterted to the database.
     */
    public void insert(Task task) { taskRepository.insert(task); }

    /**
     * Deletes the task from the database.
     *
     * @param task the task which is to be deleted.
     */
    public void delete(Task task) { taskRepository.delete(task); }

    /**
     * Returns all goals from the database.
     *
     * @return all goals.
     */
    public LiveData<List<Goal>> getAllGoals() { return allGoals; }

    /**
     * Checks if a goal with the given name already exists in the database.
     *
     * @param goalName the name which is checked.
     * @return true if a goal with the given name exists, false if not.
     */
    public LiveData<Boolean> checkIfGoalExists(String goalName) { return goalRepository.checkIfGoalExists(goalName); }

    /**
     * Inserts a goal to the database.
     *
     * @param goal the goal which is to be inserted.
     */
    public void insert(Goal goal) { goalRepository.insert(goal); }

    /**
     * Deletes a goal from the database.
     *
     * @param goal the goal which is to be deleted.
     */
    public void delete(Goal goal) { goalRepository.delete(goal); }

    /**
     * Deletes a goal by the goal's name from the database.
     *
     * @param goalName the name of the goal to be deleted.
     */
    public void deleteGoalByName(String goalName) { goalRepository.deleteGoalByName(goalName); }

    /**
     * Updates the goal's done time.
     *
     * @param gId the id of the goal which is updated.
     * @param goalDone the done time which is updated to the goal.
     */
    public void update(int gId, float goalDone) {
        goalRepository.update(gId, goalDone);
    }

    /**
     * Updates the goal's done time every second when timer service is started for a goal.
     *
     * @param gId the id of the goal which's time is updated.
     */
    public void goalTimeTick(int gId) {
        goalRepository.goalTimeTick(gId);
    }
}
