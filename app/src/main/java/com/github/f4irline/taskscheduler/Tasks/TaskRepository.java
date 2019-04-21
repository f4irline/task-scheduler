package com.github.f4irline.taskscheduler.Tasks;

import android.app.Application;
import android.os.AsyncTask;

import com.github.f4irline.taskscheduler.DatabaseImpl;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        DatabaseImpl db = DatabaseImpl.getTaskDatabase(application);
        mTaskDao = db.taskDao();
        allTasks = mTaskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Boolean> checkIfTaskExists(String taskName) {
        return mTaskDao.checkIfTaskExists(taskName);
    }

    public void insert (Task word) {
        new TaskRepository.insertAsyncTask(mTaskDao).execute(word);
    }

    public void delete (Task task) { new TaskRepository.deleteAsyncTask(mTaskDao).execute(task); }

    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        deleteAsyncTask(TaskDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
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
