package com.github.f4irline.taskscheduler.Tasks;

import android.app.Application;
import android.os.AsyncTask;

import com.github.f4irline.taskscheduler.DatabaseImpl;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Manages the usage of the tasks database.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
public class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> allTasks;

    /**
     * Constructs the repository and gets the handle to the tasks database.
     *
     * @param application the context where the database is used.
     */
    public TaskRepository(Application application) {
        DatabaseImpl db = DatabaseImpl.getTaskDatabase(application);
        mTaskDao = db.taskDao();
        allTasks = mTaskDao.getAllTasks();
    }

    /**
     * Returns all tasks.
     *
     * @return all tasks.
     */
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    /**
     * Checks if task with a certain name already exists in the database.
     *
     * @param taskName the name of the task which is checked.
     * @return true if a task with the taskName exists, false if not.
     */
    public LiveData<Boolean> checkIfTaskExists(String taskName) {
        return mTaskDao.checkIfTaskExists(taskName);
    }

    /**
     * Inserts a new task to the task database.
     *
     * @param task the task to be added.
     */
    public void insert (Task task) {
        new TaskRepository.insertAsyncTask(mTaskDao).execute(task);
    }

    /**
     * Deletes a task from the task database.
     *
     * @param task the task to be deleted.
     */
    public void delete (Task task) { new TaskRepository.deleteAsyncTask(mTaskDao).execute(task); }

    /**
     * Handles deleting a task asynchronously, to avoid "locking" the UI when running tasks
     * that may take some time.
     */
    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        /**
         * Constructor to initialize the TaskDao which is used for the delete action.
         * @param dao the TaskDao used.
         */
        deleteAsyncTask(TaskDao dao) { mAsyncTaskDao = dao; }

        /**
         * Does the delete action running in background.
         *
         * @param params the task to be deleted.
         * @return null.
         */
        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     * Handles inserting a new task asynchronously, to avoid "locking" the UI when running tasks
     * that may take some time.
     */
    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao mAsyncTaskDao;

        /**
         * Constructor to initialize the TaskDao which is used for the insert action.
         * @param dao the TaskDao used.
         */
        insertAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * Does the insert action running in the background.
         *
         * @param params the task to be inserted.
         * @return null.
         */
        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
