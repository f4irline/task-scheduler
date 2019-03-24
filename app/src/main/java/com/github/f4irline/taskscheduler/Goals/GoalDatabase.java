package com.github.f4irline.taskscheduler.Goals;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Goal.class}, version = 1, exportSchema = false)
public abstract class GoalDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();

    private static volatile GoalDatabase INSTANCE;

    static GoalDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GoalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GoalDatabase.class, "goal_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final GoalDao mDao;

        PopulateDbAsync(GoalDatabase db) {
            mDao = db.goalDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Goal goal = new Goal("Code", 20);
            mDao.insert(goal);
            goal = new Goal("Read", 30);
            mDao.insert(goal);
            goal = new Goal("Exercise", 20);
            mDao.insert(goal);
            return null;
        }
    }
}
