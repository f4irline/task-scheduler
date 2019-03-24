package com.github.f4irline.taskscheduler.Goals;

import android.app.Application;
import android.os.AsyncTask;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Goals.GoalDao;
import com.github.f4irline.taskscheduler.Goals.GoalDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class GoalRepository {
    private GoalDao mGoalDao;
    private LiveData<List<Goal>> allGoals;

    GoalRepository(Application application) {
        GoalDatabase db = GoalDatabase.getDatabase(application);
        mGoalDao = db.goalDao();
        allGoals = mGoalDao.getAllGoals();
    }

    LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }

    public void insert (Goal goal) {
        new insertAsyncTask(mGoalDao).execute(goal);
    }

    public void delete (Goal goal) { new deleteAsyncTask(mGoalDao).execute(goal); }

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
