package com.example.fitness_tracker_app.run.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitness_tracker_app.R;

public class HistoryRunFragment extends Fragment implements HistoryContract.View{
    HistoryContract.Presenter mPresenter;
    RecyclerView recyclerView;
    TextView emptyState;

    public HistoryRunFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_menu:
                showDeleteConfirmationDialog();
                return true;
        }

       return super.onOptionsItemSelected(item);
    }


    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.delete_dialog));
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.deleteAll();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_run, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyState = view.findViewById(R.id.history_empty_state);
        setView();
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("History", "OnResume");
        setView();
    }

    public void setView() {
        mPresenter = new HistoryPresenter(this, getContext());
        HistoryAdapter historyAdapter = new HistoryAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        historyAdapter.setValues(mPresenter.getAllRoutes());
        emptyState.setVisibility(View.GONE);
        recyclerView.setAdapter(historyAdapter);
    }

    @Override
    public void setEmptyState() {
        emptyState.setVisibility(View.VISIBLE);
    }
}