package com.github.f4irline.taskscheduler.Goals;

import com.github.f4irline.taskscheduler.Tasks.Task;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goal_table")
public class Goal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int goalId;

    @ColumnInfo(name = "goal")
    public String goal;

    @ColumnInfo(name = "goal_time")
    public float goalTime;

    @ColumnInfo(name = "goal_done")
    public float goalDone;

    public Goal(String goal, float goalTime) {
        this.goal = goal;
        this.goalTime = goalTime;
        this.goalDone = 0;
    }

    public String getGoal() {
        return this.goal;
    }

    public int getGoalId() { return this.goalId; }

    public float getGoalTime() {
        return this.goalTime;
    }

    public float getGoalDone() { return this.goalDone; }

    public void setGoalDone(float goalDone) {
        this.goalDone = goalDone;
    }

    @Override
    public String toString() {
        return this.goal;
    }
}
