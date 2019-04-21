package com.github.f4irline.taskscheduler.Tasks;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int taskId;

    @ColumnInfo(name = "task")
    public String task;

    public Task(String task) {
        this.task = task;
    }

    public String getTask() {
        return this.task;
    }

    public int getTaskId() { return this.taskId; }

    @Override
    public String toString() {
        return this.task;
    }
}
