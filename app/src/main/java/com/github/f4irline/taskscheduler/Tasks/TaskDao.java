package com.github.f4irline.taskscheduler.Tasks;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Data access object implementation, which defines the different queries related to the tasks
 * entities.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
@Dao
public interface TaskDao {
    /**
     * Queries the room database to check if a task with the given name exists already.
     *
     * @param taskName the name of the task.
     * @return true if exists, false if not.
     */
    @Query("SELECT EXISTS(SELECT * from task_table WHERE LOWER(task) = LOWER(:taskName))")
    LiveData<Boolean> checkIfTaskExists(String taskName);

    /**
     * Gets all task from the database.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    /**
     * Inserts a task to the database.
     *
     * @param task the task to be inserted.
     */
    @Insert
    void insert(Task task);

    /**
     * Deletes all tasks from the task database.
     */
    @Query("DELETE FROM task_table")
    void deleteAll();

    /**
     * Deletes a specified task from the task database.
     *
     * @param task the task to be removed.
     */
    @Delete
    void delete(Task task);
}
