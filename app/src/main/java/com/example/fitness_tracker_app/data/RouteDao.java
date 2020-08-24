package com.example.fitness_tracker_app.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitness_tracker_app.Route;

import java.util.List;

    @Dao
    public interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void save(Route route);

    @Delete
    void delete(Route route);

    @Query("DELETE FROM route_info")
    void deleteAll();

    @Query("SELECT * FROM route_info ")
    List<Route> getAllRoutes();

    }
