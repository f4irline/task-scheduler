package com.github.f4irline.taskscheduler.Tasks;

import android.app.Application;
import android.os.AsyncTask;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Goals.GoalDao;
import com.github.f4irline.taskscheduler.Goals.GoalDatabase;
import com.github.f4irline.taskscheduler.Goals.GoalRepository;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TaskRepository {
    private TaskDao mGoalDao;
    private LiveData<List<Task>> allTasks;

    TaskRepository(Application application) {
        TaskDatabase db = TaskDatabase.getDatabase(application);
        mGoalDao = db.taskDao();
        allTasks = mGoalDao.getAllTasks();
    }

    LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert (Task word) {
        new TaskRepository.insertAsyncTask(mGoalDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao mAsyncTaskDao;

        insertAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
