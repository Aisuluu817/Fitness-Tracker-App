package com.example.fitness_tracker_app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName ="route_info")
public class Route implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    @NonNull
    private String routeName;
    @NonNull
    private String time;
    private double distance;
    @NonNull
    private double speed;
    private String date;


    public Route(@NonNull String routeName,@NonNull String time, double distance, @NonNull double speed, String date) {
        this.routeName = routeName;
        this.time = time;
        this.distance = distance;
        this.speed = speed;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getRouteName() {
        return routeName;
    }

    public String getTime() {
        return time;
    }

    public double getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }

    public String getDate() {
        return date;
    }
}
