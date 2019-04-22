package com.github.f4irline.taskscheduler.Tasks;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The entity of the tasks which are saved to the Room database.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
@Entity(tableName = "task_table")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int taskId;

    @ColumnInfo(name = "task")
    public String task;

    /**
     * Constructor for the task.
     *
     * @param task the name of the task created.
     */
    public Task(String task) {
        this.task = task;
    }

    /**
     * Returns the task entity name.
     *
     * @return the name of the task entity.
     */
    public String getTask() {
        return this.task;
    }

    /**
     * Returns the task entity id.
     *
     * @return the id of the task entity.
     */
    public int getTaskId() { return this.taskId; }

    /**
     * Returns the task entity name when the toString() is called.
     *
     * @return the name of the task.
     */
    @Override
    public String toString() {
        return this.task;
    }
}
