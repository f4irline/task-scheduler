package com.github.f4irline.taskscheduler.Goals;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Data access object implementation, which defines the different queries related to the goal
 * entities.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
@Dao
public interface GoalDao {
    /**
     * Queries the room database to check if a goal with the given name exists already.
     *
     * @param goalName the name of the goal.
     * @return true if exists, false if not.
     */
    @Query("SELECT EXISTS(SELECT * from goal_table WHERE goal = :goalName)")
    LiveData<Boolean> checkIfGoalExists(String goalName);

    /**
     * Gets all goals from the database.
     *
     * @return all goals.
     */
    @Query("SELECT * FROM goal_table")
    LiveData<List<Goal>> getAllGoals();

    /**
     * Returns the total goal time of all goals summed together from the database.
     *
     * @return the total goal time of all goals summed together.
     */
    @Query("SELECT SUM(goal_time) FROM goal_table")
    float getTotalGoalTime();

    /**
     * Inserts a new goal to the database.
     *
     * @param goal the goal to be inserted.
     */
    @Insert
    void insert(Goal goal);

    /**
     * Deletes all goals from the database.
     */
    @Query("DELETE FROM goal_table")
    void deleteAll();

    /**
     * Returns the goal done time from a specified goal.
     *
     * @param gId the id of the goal which's goal done time is requested.
     * @return the goal done time from the goal.
     */
    @Query("SELECT goal_done FROM goal_table WHERE goalId = :gId")
    float getGoalDone(int gId);

    /**
     * Deletes a goal from the database.
     *
     * @param goal the goal to be deleted.
     */
    @Delete
    void delete(Goal goal);

    /**
     * Deletes a goal by it's name from the goal database.
     *
     * @param goalName the name of the goal to be deleted.
     */
    @Query("DELETE FROM goal_table WHERE goal = :goalName")
    void deleteByName(String goalName);

    /**
     * Adds time done to a goal.
     *
     * @param gId the id of the goal updated.
     * @param goalDone the time done to be added to the goal.
     * @return the id of the goal updated.
     */
    @Query("UPDATE goal_table SET goal_done = goal_done + :goalDone WHERE goalId = :gId")
    int updateGoal(int gId, float goalDone);

    /**
     * Updates the goal done time with a new value.
     *
     * @param gId the id of the goal updated.
     * @param goalDoneTotal the total done time of the goal.
     * @return the id of the goal updated.
     */
    @Query("UPDATE goal_table SET goal_done = :goalDoneTotal WHERE goalId = :gId")
    int updateGoalTotal(int gId, float goalDoneTotal);

    /**
     * Adds a second to the goal done time for a specified goal.
     *
     * @param gId the id of the goal updated.
     * @return the id of the goal updated.
     */
    @Query("UPDATE goal_table SET goal_done = (goal_done + (1.0 / 3600.0)) WHERE goalId = :gId")
    int goalTimeTick(int gId);
}
