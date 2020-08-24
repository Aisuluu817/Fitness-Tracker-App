package com.example.fitness_tracker_app.run.history;

import android.content.Context;
import android.media.tv.TvContentRating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_tracker_app.R;
import com.example.fitness_tracker_app.Route;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    Context context;
    private LayoutInflater layoutInflater;
    private List<Route> routes = new ArrayList<>();

    public HistoryAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_run, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(routes.get(position));

    }


    @Override
    public int getItemCount() {
        if (routes == null) {
            return 0;
        } else {
            return routes.size();
        }
    }

    public void setValues(List<Route> items) {
        if (items != null){
            routes.clear();
            routes.addAll(items);
        }
        notifyDataSetChanged();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView routeName;
        TextView routeDistance;
        TextView routeTime;
        TextView routeDate;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.history_name_text_view);
            routeDistance = itemView.findViewById(R.id.history_distance_text_view);
            routeTime = itemView.findViewById(R.id.history_time_text_view);
            routeDate = itemView.findViewById(R.id.history_date_text_view);
        }

        public void bind(Route route) {
            routeName.setText(route.getRouteName());
            if (route.getRouteName().equals("")){
                routeName.setText("Unknown");
            }
            routeDistance.setText("route length: " + route.getDistance() + " km");
            routeTime.setText(route.getTime());
            routeDate.setText(route.getDate());
        }
    }
}
