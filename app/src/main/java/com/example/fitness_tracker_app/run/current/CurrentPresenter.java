package com.example.fitness_tracker_app.run.current;


import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.example.fitness_tracker_app.Route;
import com.example.fitness_tracker_app.data.RouteDao;
import com.example.fitness_tracker_app.data.RouteDatabase;

public class CurrentPresenter implements CurrentContract.Presenter {
    private RouteDao routeDao;
    private CurrentContract.View view;


    public CurrentPresenter(CurrentContract.View view, Context application){
        this.view = view;
        routeDao =  RouteDatabase.getInstance(application).routeDao();
    }

    @Override
    public void save(Route route) {
        new SaveRouteAsyncTask(routeDao).execute(route);
    }

    private class SaveRouteAsyncTask  extends AsyncTask<Route, Void, Void> {
        private RouteDao routeDao;

        public SaveRouteAsyncTask(RouteDao routeDao) {
            this.routeDao = routeDao;
        }

        @Override
        protected Void doInBackground(Route... routes) {
            routeDao.save(routes[0]);
            return null;
        }
    }

}
