package com.example.fitness_tracker_app.run.history;

import com.example.fitness_tracker_app.Route;

import java.util.List;

public interface HistoryContract {
    interface View{
        void setView();
        void setEmptyState();

    }
    interface Presenter{
        void deleteAll();
        List<Route> getAllRoutes();
        }
    }

