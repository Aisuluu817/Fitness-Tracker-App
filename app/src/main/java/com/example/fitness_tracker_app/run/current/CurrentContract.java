package com.example.fitness_tracker_app.run.current;

import com.example.fitness_tracker_app.Route;

public interface CurrentContract {


     interface View{
     }
     interface Presenter{
         void save(Route route);
     }
}
