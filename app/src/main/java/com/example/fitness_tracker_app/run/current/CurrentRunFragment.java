package com.example.fitness_tracker_app.run.current;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness_tracker_app.R;
import com.example.fitness_tracker_app.Route;
import com.example.fitness_tracker_app.run.history.HistoryPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CurrentRunFragment extends Fragment implements OnMapReadyCallback, CurrentContract.View {
    private static final String LOG_TAG = "CurrentRunFragment";
    Location currentLocation;
    CurrentContract.Presenter mPresenter;
    ArrayList<Location> locations = new ArrayList<>();
    Chronometer chronometer;
    GoogleMap mGoogleMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    MapView mapView;
    View view;
    String routeName;
    String time;
    long sTime, eTime, dTime;
    int speed = 0;
    double distance = 0;
    double timeSpeed;
    Button startButton;
    TextView currentDistanceTextView;
    TextView currentSpeedTextView;
    Task<Location> task;
    private static final int REQUEST_CODE = 101;
    private boolean permissionGranted = false;
    boolean gps_enabled = false;
    LocationManager lm;

    public CurrentRunFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());
        view = inflater.inflate(R.layout.fragment_current_run, container, false);
        mPresenter = new CurrentPresenter(this, getContext());
        permissionRequest();
        mapView = view.findViewById(R.id.map_view);
        startButton = view.findViewById(R.id.start_button);
        chronometer = view.findViewById(R.id.time_current_run);
        currentDistanceTextView = view.findViewById(R.id.current_distance);
        currentSpeedTextView = view.findViewById(R.id.current_speed);
        lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        gpsNetworkCheck();

        if (!gps_enabled){
            gpsDialog();
        }

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (startButton.getText().equals("start")) {
                        if (permissionGranted && gps_enabled) {
                            startRoute();
                        }
                        else {
                            permissionRequest();
                            gpsNetworkCheck();
                        }
                    } else if (startButton.getText().equals("save")) {
                        showNameAlertDialog();
                        startButton.setText("start");
                        locations.clear();

                    } else {
                        stopRoute();

                    }
                }
            });

        return view;

    }

    private void gpsDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.gps_network_not_enabled)
                .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                    .show();
    }

    private void gpsNetworkCheck() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.e(LOG_TAG, gps_enabled + " ");

        } catch(Exception ex) {}

    }

    private void setUpView() {
        currentSpeedTextView.setText("0 m/s");
        currentDistanceTextView.setText("0.0 km");
        chronometer.setBase(SystemClock.elapsedRealtime());
        mapView.onStart();
        mapView.getMapAsync(this);
    }

    private void stopRoute() {
        chronometer.stop();
        eTime = Calendar.getInstance().getTimeInMillis();
        dTime = eTime - sTime;
        Log.e(LOG_TAG, dTime + "");
        long hh = TimeUnit.MILLISECONDS.toHours(dTime);
        long mn = TimeUnit.MILLISECONDS.toMinutes(dTime) - hh *60;
        long s = TimeUnit.MILLISECONDS.toSeconds(dTime) - hh *60 * 60 - mn * 60 ;
        time = hh+"h:" + mn +"m:" + s + "s";
        timeSpeed = (int) dTime / 1000;
        setTextViews();
        fetchLocation();
        locations.add(currentLocation);
        getDistance();
    }


    private void setTextViews() {
        currentDistanceTextView.setText(distance + " km");
        currentSpeedTextView.setText(speed + "m/s");
        startButton.setText("save");
    }


    private void getDistance() {
        float result = 0;
        double startLat = locations.get(0).getLatitude();
        double startLong = locations.get(0).getLongitude();
        double endLat = locations.get(1).getLatitude();
        double endLong = locations.get(1).getLongitude();
        Location.distanceBetween(startLat, startLong, endLat, endLong, new float[]{result});
        draw(locations);
        distance = distance + result;
        if (distance == 0){
            speed = 0;
        }
        else {
            speed =  (int) Math.ceil((distance * 1000) / timeSpeed);
            currentDistanceTextView.setText(distance + " km");
            currentSpeedTextView.setText(speed + " m/s");
        }
    }

    private void startRoute() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        sTime = Calendar.getInstance().getTimeInMillis();
        fetchLocation();
        locations.add(currentLocation);
        startButton.setText("stop");
    }

    private void draw(ArrayList<Location> locations) {
        int length = locations.size();
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.fillColor(Color.BLUE);
        polygonOptions.strokeWidth(8);


        for (int i = 0; i < length; i++ ){
            polygonOptions.add(new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude()));
        }
        mGoogleMap.addPolygon(polygonOptions);

    }

    private void permissionRequest() {
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            task = fusedLocationProviderClient.getLastLocation();

            permissionGranted = true;
            fetchLocation();
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                showPermissionAlertDialog();
            }
            else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            }
        }
    }

    private void showPermissionAlertDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.location_permission)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                , REQUEST_CODE);
                    }
                }).create()
                  .show();
              }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    fetchLocation();
                }
                else {
                    Toast.makeText(getContext(), "Could not find current location", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void fetchLocation(){
    task.addOnSuccessListener(new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if (location != null) {
                currentLocation = location;
                mapView.getMapAsync(CurrentRunFragment.this);
            }
        }
    });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        Log.e(LOG_TAG, "onMapReady");
        LatLng latLng = new LatLng(currentLocation.getLatitude(), this.currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        googleMap.addMarker(markerOptions);

    }

    private void showNameAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.name_dialog));
        final EditText editText = new EditText(getContext());
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setMinWidth(30);
        builder.setView(editText);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                routeName = editText.getText().toString();
                saveMethod(routeName);
                setUpView();
            }
        });
        builder.show();
    }

    private void saveMethod(String routeName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM dd");
        Date date = new Date();
        mPresenter.save(new Route(routeName, time, distance, speed, formatter.format(date)));

    }

}
