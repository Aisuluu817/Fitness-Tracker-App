package com.example.fitness_tracker_app.run.history;

import android.content.Context;
import android.os.AsyncTask;

import com.example.fitness_tracker_app.Route;
import com.example.fitness_tracker_app.data.RouteDao;
import com.example.fitness_tracker_app.data.RouteDatabase;


import java.util.List;

public class HistoryPresenter implements HistoryContract.Presenter{
    private RouteDao routeDao;
    private HistoryContract.View view;

    public HistoryPresenter(HistoryContract.View view, Context application) {
        this.view = view;
        routeDao = RouteDatabase.getInstance(application).routeDao();
    }

    @Override
    public void deleteAll() {
        new DeleteAllAsyncTask(routeDao).execute();
        view.setView();
    }

    @Override
    public List<Route> getAllRoutes() {
        return routeDao.getAllRoutes();
    }



    private class DeleteAllAsyncTask  extends AsyncTask<Route, Void, Void> {
        private RouteDao routeDao;
        public DeleteAllAsyncTask(RouteDao routeDao) {
            this.routeDao = routeDao;
        }

        @Override
        protected Void doInBackground(Route... routes) {
            routeDao.deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            view.setView();
            view.setEmptyState();
            super.onPostExecute(aVoid);
        }
    }
}
