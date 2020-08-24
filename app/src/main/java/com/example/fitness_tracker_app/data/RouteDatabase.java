package com.example.fitness_tracker_app.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitness_tracker_app.Route;

@Database(entities = {Route.class}, version = 2)
public abstract class RouteDatabase extends RoomDatabase {
    private static RouteDatabase routeDatabase;

    public abstract RouteDao routeDao();

    public static synchronized RouteDatabase getInstance(Context context){
        if (routeDatabase == null){
            routeDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    RouteDatabase.class, "route_history")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return routeDatabase;
    }

}
