package com.github.f4irline.taskscheduler.Tasks;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @Insert
    void insert(Task Task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Delete
    void delete(Task task);
}
