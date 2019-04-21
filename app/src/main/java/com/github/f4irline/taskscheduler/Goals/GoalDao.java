package com.github.f4irline.taskscheduler.Goals;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface GoalDao {

    @Query("SELECT EXISTS(SELECT * from goal_table WHERE goal = :goalName)")
    LiveData<Boolean> checkIfGoalExists(String goalName);

    @Query("SELECT * FROM goal_table")
    LiveData<List<Goal>> getAllGoals();

    @Query("SELECT SUM(goal_time) FROM goal_table")
    float getTotalGoalTime();

    @Insert
    void insert(Goal goal);

    @Query("DELETE FROM goal_table")
    void deleteAll();

    @Query("SELECT goal_done FROM goal_table WHERE goalId = :gId")
    float getGoalDone(int gId);

    @Delete
    void delete(Goal goal);

    @Query("UPDATE goal_table SET goal_done = goal_done + :goalDone WHERE goalId = :gId")
    int updateGoal(int gId, float goalDone);

    @Query("UPDATE goal_table SET goal_done = :goalDoneTotal WHERE goalId = :gId")
    int updateGoalTotal(int gId, float goalDoneTotal);

    @Query("UPDATE goal_table SET goal_done = (goal_done + (1.0 / 3600.0)) WHERE goalId = :gId")
    int goalTimeTick(int gId);
}
