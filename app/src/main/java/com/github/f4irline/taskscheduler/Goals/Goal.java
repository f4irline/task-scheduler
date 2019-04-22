package com.github.f4irline.taskscheduler.Goals;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The entity of the goals which are saved to the Room database.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
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

    /**
     * Constructor for the goal.
     *
     * @param goal the name of the goal.
     * @param goalTime the time for the goal to be done.
     */
    public Goal(String goal, float goalTime) {
        this.goal = goal;
        this.goalTime = goalTime;
        this.goalDone = 0;
    }

    /**
     * Returns the name of the goal.
     *
     * @return the name of the goal.
     */
    public String getGoal() {
        return this.goal;
    }

    /**
     * Returns the id of the goal.
     *
     * @return the id of the goal.
     */
    public int getGoalId() { return this.goalId; }

    /**
     * Returns the total goal time.
     *
     * @return the total goal time.
     */
    public float getGoalTime() {
        return this.goalTime;
    }

    /**
     * Returns the goal done time.
     *
     * @return the goal done time.
     */
    public float getGoalDone() { return this.goalDone; }

    /**
     * Sets the goal done time.
     *
     * @param goalDone the goal done time.
     */
    public void setGoalDone(float goalDone) {
        this.goalDone = goalDone;
    }

    /**
     * Returns the goal entity name when the toString() is called.
     *
     * @return the name of the goal.
     */
    @Override
    public String toString() {
        return this.goal;
    }
}
