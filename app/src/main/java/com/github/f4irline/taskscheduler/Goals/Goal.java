package com.github.f4irline.taskscheduler.Goals;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goal_table")
public class Goal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int goalId;

    @ColumnInfo(name = "goal")
    public String goal;

    @ColumnInfo(name = "time")
    public float time;

    public Goal(String goal, float time) {
        this.goal = goal;
        this.time = time;
    }

    public String getGoal() {
        return this.goal;
    }

    public float getTime() {
        return this.time;
    }
}
