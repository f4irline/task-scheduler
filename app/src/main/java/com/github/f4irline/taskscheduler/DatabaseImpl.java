package com.github.f4irline.taskscheduler;

import android.content.Context;

import com.github.f4irline.taskscheduler.Goals.Goal;
import com.github.f4irline.taskscheduler.Goals.GoalDao;
import com.github.f4irline.taskscheduler.Tasks.Task;
import com.github.f4irline.taskscheduler.Tasks.TaskDao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Class which is initializes the Room database.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0324
 */
@Database(entities = {Goal.class, Task.class}, version = 1, exportSchema = false)
public abstract class DatabaseImpl extends RoomDatabase {
    public abstract GoalDao goalDao();
    public abstract TaskDao taskDao();

    private static volatile DatabaseImpl GOAL_INSTANCE;
    private static volatile DatabaseImpl TASK_INSTANCE;

    /**
     * Initializes the goal database (or rather table).
     *
     * @param context the context where the method is called from.
     * @return the instance of the implemented goal database.
     */
    public static DatabaseImpl getGoalDatabase(final Context context) {
        if (GOAL_INSTANCE == null) {
            synchronized (DatabaseImpl.class) {
                if (GOAL_INSTANCE == null) {
                    GOAL_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseImpl.class, "goal_database")
                            .addCallback(goalDatabaseCallback)
                            .build();
                }
            }
        }
        return GOAL_INSTANCE;
    }

    /**
     * Initializes the task database (or rather table).
     *
     * @param context the context where the method is called from.
     * @return the instance of the implemented task database.
     */
    public static DatabaseImpl getTaskDatabase(final Context context) {
        if (TASK_INSTANCE == null) {
            synchronized (DatabaseImpl.class) {
                if (TASK_INSTANCE == null) {
                    TASK_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseImpl.class, "task_database")
                            .addCallback(taskDatabaseCallback)
                            .build();
                }
            }
        }
        return TASK_INSTANCE;
    }

    /**
     * The callback which is called when the goal database has been initialized.
     */
    private static RoomDatabase.Callback goalDatabaseCallback =
            new RoomDatabase.Callback(){

                /**
                 * Called when the database has been opened.
                 *
                 * @param db the opened database.
                 */
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                }
            };

    /**
     * The callback which is called when the task database has been initialized.
     */
    private static RoomDatabase.Callback taskDatabaseCallback =
            new RoomDatabase.Callback(){

                /**
                 * Called when the database has been opened.
                 *
                 * @param db the opened database.
                 */
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                }
            };
}
