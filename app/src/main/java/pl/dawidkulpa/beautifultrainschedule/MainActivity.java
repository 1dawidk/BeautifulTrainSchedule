package pl.dawidkulpa.beautifultrainschedule;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import pl.dawidkulpa.beautifultrainschedule.Algorithms.EA.EA;
import pl.dawidkulpa.beautifultrainschedule.Algorithms.EA.Organism;
import pl.dawidkulpa.beautifultrainschedule.Schedule.Schedule;
import pl.dawidkulpa.beautifultrainschedule.Stations.TrainStation;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ALL=1;
    private static final String[] STATION_NAMES={
            "Alfa", "Bravo", "Charlie", "Delta", "Echo",
            "Foxtrot", "Golf", "Hotel", "India", "Juliet",
            "Kilo", "Lima", "Mike", "November", "Oscar",
            "Papa", "Quebec", "Romeo", "Sierra", "Tango",
            "Uniform", "Victor", "Whiskey", "X-ray",
            "Yankee", "Zulu"};

    private GoogleMap map;
    private ArrayList<TrainStation> trainStations;
    private FusedLocationProviderClient fusedLocationClient;

    public static Schedule bestSchedule=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFABClick();
            }
        });

        getLocationPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

        trainStations= new ArrayList<>();
    }

    private void getLocationPermission() {
        String[] perms;
        boolean granted;

        //Check if already granted
        granted= ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (!granted) {
            perms= new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION};

            ActivityCompat.requestPermissions(this, perms, PERMISSIONS_REQUEST_ALL);
        }
    }

    public void onFABClick(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setTitle("Select algorithm");
        builder.setNegativeButton("EA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onEASelect();
            }
        });
        builder.setNeutralButton("Multi culti EA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onMultiCultiEASelect();
            }
        });
        builder.setPositiveButton("Monte Carlo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onMultiAgentMonteCarloSelect();
            }
        });

        builder.create().show();
    }

    public void onEASelect(){
        EA ea= new EA(trainStations, new EA.OnFinishListener() {
            @Override
            public void onFinish(ArrayList<Organism> organisms) {
                onEAFinished(organisms);
            }
        });

        ea.execute();
    }

    public void onMultiCultiEASelect(){

    }

    public void onMultiAgentMonteCarloSelect(){

    }

    public void onEAFinished(ArrayList<Organism> organisms){
        Organism best= organisms.get(0);

        for(int i=1; i<organisms.size(); i++){
            if(organisms.get(i).evaluate()>best.evaluate()){
                best= organisms.get(i);
            }
        }

        double points= best.evaluate();
        Toast.makeText(this, "Policzone EA: Najlepszy="+points, Toast.LENGTH_SHORT).show();

        bestSchedule= new Schedule();
        bestSchedule.parse(best.getTrains(), trainStations);


        Intent intent= new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map= googleMap;

        try {
            //Disable My Location button
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setRotateGesturesEnabled(false);

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()), 6));
                }
            });

        } catch (SecurityException se){
            Log.e("Exception", se.getMessage());
        }



        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                MainActivity.this.onInfoWindowClick(marker);
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                for(int i=0; i<trainStations.size(); i++){
                    trainStations.get(i).hideInfo();
                }
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                createNewTrainStation(map, latLng);
            }
        });
    }

    private void createNewTrainStation(GoogleMap googleMap, LatLng latLng){
        trainStations.add(new TrainStation(STATION_NAMES[trainStations.size()],
                latLng, googleMap, this));
    }

    private void onInfoWindowClick(Marker marker){
        for(int i=0; i<trainStations.size(); i++){
            if(trainStations.get(i).compareMarker(marker)){
                trainStations.get(i).openEditDialog(this);
            }
        }
    }
}
