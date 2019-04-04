package com.github.f4irline.taskscheduler.Goals;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.github.f4irline.taskscheduler.DatabaseImpl;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class GoalRepository {
    private GoalDao mGoalDao;
    private LiveData<List<Goal>> allGoals;
    private ExecutorService executor;

    public GoalRepository(Application application) {
        DatabaseImpl db = DatabaseImpl.getGoalDatabase(application);
        mGoalDao = db.goalDao();
        allGoals = mGoalDao.getAllGoals();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }

    public void getTotalGoalTime() {
        executor.execute(() -> {
            mGoalDao.getTotalGoalTime();
        });
    }

    public void insert (Goal goal) {
        new insertAsyncTask(mGoalDao).execute(goal);
    }

    public void delete (Goal goal) { new deleteAsyncTask(mGoalDao).execute(goal); }

    public void update (int gId, float goalDone) {
        executor.execute(() -> {
            Log.d("GoalRepository", "Id: "+mGoalDao.updateGoal(gId, goalDone));
        });
    }

    private static class deleteAsyncTask extends AsyncTask<Goal, Void, Void> {
        private GoalDao mAsyncTaskDao;

        deleteAsyncTask(GoalDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDao mAsyncTaskDao;

        insertAsyncTask(GoalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
