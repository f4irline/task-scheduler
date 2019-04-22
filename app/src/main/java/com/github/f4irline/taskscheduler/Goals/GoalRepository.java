package com.github.f4irline.taskscheduler.Goals;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.github.f4irline.taskscheduler.DatabaseImpl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

/**
 * Manages the usage of the goals database.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
public class GoalRepository {
    private GoalDao mGoalDao;
    private LiveData<List<Goal>> allGoals;
    private ExecutorService executor;

    /**
     * Constructs the repository and gets the handle to the goals database.
     *
     * @param application the context where the database is used.
     */
    public GoalRepository(Application application) {
        DatabaseImpl db = DatabaseImpl.getGoalDatabase(application);
        mGoalDao = db.goalDao();
        allGoals = mGoalDao.getAllGoals();
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Returns all goals.
     *
     * @return all goals.
     */
    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }

    /**
     * Executes a query to request the total goal time.
     */
    public void getTotalGoalTime() {
        executor.execute(() -> {
            mGoalDao.getTotalGoalTime();
        });
    }

    /**
     * Checks if goal with given name already exists.
     *
     * @param goalName the name which is checked.
     * @return true if exists, false if not.
     */
    public LiveData<Boolean> checkIfGoalExists(String goalName) {
        return mGoalDao.checkIfGoalExists(goalName);
    }

    /**
     * Inserts a new goal.
     *
     * @param goal goal to be inserted.
     */
    public void insert (Goal goal) {
        new insertAsyncTask(mGoalDao).execute(goal);
    }

    /**
     * Deletes a goal.
     *
     * @param goal goal to be deleted.
     */
    public void delete (Goal goal) { new deleteAsyncTask(mGoalDao).execute(goal); }

    /**
     * Updates a goal's done time.
     *
     * @param gId the id of the goal updated.
     * @param goalDone the goal done time to be added to the goal.
     */
    public void update (int gId, float goalDone) {
        executor.execute(() -> {
            Log.d("GoalRepository", "Id: "+mGoalDao.updateGoal(gId, goalDone));
        });
    }

    /**
     * Updates a goal's total time done.
     *
     * @param gId the id of the goal to be updated.
     * @param goalDoneTotal the goal done time to be replaced to the goal.
     */
    public void updateGoalTotal (int gId, float goalDoneTotal) {
        executor.execute(() -> {
            mGoalDao.updateGoalTotal(gId, goalDoneTotal);
        });
    }

    /**
     * Adds a second to the goal's time done.
     *
     * @param gId the id of the goal updated.
     */
    public void goalTimeTick (int gId) {
        executor.execute(() -> {
            mGoalDao.goalTimeTick(gId);
        });
    }

    /**
     * Deletes a goal with the given name.
     *
     * @param goalName the given name of the goal to be deleted.
     */
    public void deleteGoalByName(String goalName) {
        executor.execute(() -> {
            mGoalDao.deleteByName(goalName);
        });
    }

    /**
     * Handles deleting a goal asynchronously, to avoid "locking" the UI when running tasks
     * that may take some time.
     */
    private static class deleteAsyncTask extends AsyncTask<Goal, Void, Void> {
        private GoalDao mAsyncTaskDao;

        /**
         * Constructor to initialize the GoalDao which is used for the delete action.
         * @param dao the GoalDao used.
         */
        deleteAsyncTask(GoalDao dao) { mAsyncTaskDao = dao; }

        /**
         * Does the delete action running in background.
         *
         * @param params the goal to be deleted.
         * @return null.
         */
        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     * Handles inserting a new goal asynchronously, to avoid "locking" the UI when running tasks
     * that may take some time.
     */
    private static class insertAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDao mAsyncTaskDao;

        /**
         * Constructor to initialize the GoalDao which is used for the insert action.
         * @param dao the TaskDao used.
         */
        insertAsyncTask(GoalDao dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * Does the insert action running in the background.
         *
         * @param params the goal to be inserted.
         * @return null.
         */
        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
