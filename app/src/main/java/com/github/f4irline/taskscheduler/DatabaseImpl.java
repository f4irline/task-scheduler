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

@Database(entities = {Goal.class, Task.class}, version = 1, exportSchema = false)
public abstract class DatabaseImpl extends RoomDatabase {
    public abstract GoalDao goalDao();
    public abstract TaskDao taskDao();

    private static volatile DatabaseImpl GOAL_INSTANCE;
    private static volatile DatabaseImpl TASK_INSTANCE;

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

    private static RoomDatabase.Callback goalDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                }
            };

    private static RoomDatabase.Callback taskDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                }
            };
}
