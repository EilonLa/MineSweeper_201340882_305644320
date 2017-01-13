package fragments;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Iterator;
import java.util.List;

import activities.LoginActivity;
import db.DataRow;
import logic.Compass;

public class MyMapFragment extends FragmentActivity implements SensorEventListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    private GoogleApiClient googleApiClient;
    private GoogleMap map;
    private Location lastLocation;
    private LocationRequest locationRequest;
    public static final String TAG = MyMapFragment.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private FloatingActionButton back;
    private MyMapFragment myMapFragment;
    private Compass compass;
    private Sensor sensor;
    private Sensor sensorMagneticField;
    private SensorManager sensorManager;
    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;
    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        myMapFragment = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        back = (FloatingActionButton)findViewById(R.id.back_map);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                myMapFragment.onBackPressed();
            }
        });


        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];
        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this,sensorMagneticField,sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                //now get the location the phone is facing
                valuesAccelerometer[0] = sensorEvent.values[0];
                valuesAccelerometer[1] = sensorEvent.values[1];
                valuesAccelerometer[2] = sensorEvent.values[2];
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                valuesMagneticField[0] = sensorEvent.values[0];
                valuesMagneticField[1] = sensorEvent.values[1];
                valuesMagneticField[2] = sensorEvent.values[2];
                break;
        }
        if (compass == null){
            compass = new Compass(this);
            compass.findViewById(R.id.compass_game);
            setCompass(compass);
        }

        if (SensorManager.getRotationMatrix(matrixR,matrixI,valuesAccelerometer,valuesMagneticField)){
            SensorManager.getOrientation(matrixR,matrixValues);
            updateCompass(matrixValues[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        DataRow lastGame = LoginActivity.database.getLastGameFromDB();
        if (lastGame != null)
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lastGame.getLatidude()),Double.parseDouble(lastGame.getLongtitude()))));
        List<DataRow> allScores = LoginActivity.database.getAllGames();
        Iterator itr = allScores.iterator();

        while (itr.hasNext()) {
            DataRow temp = (DataRow) itr.next();
            LatLng tempLoc = new LatLng(Double.parseDouble(temp.getLatidude()), Double.parseDouble(temp.getLongtitude()));
            MarkerOptions tempMarker = new MarkerOptions().position(tempLoc).title("Player "+temp.getmName() + " score: " + temp.getScore()).snippet("Level: " + temp.getLevel());
            if (temp.getLevel() == 1)
                tempMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            if (temp.getLevel() == 2)
                tempMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            if (temp.getLevel() == 3)
                tempMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            map.addMarker(tempMarker);
        }
    }

    public void setCompass(Compass compass) {
        this.compass = compass;
        LinearLayout l = (LinearLayout) findViewById(R.id.compass);
        l.addView(compass);
    }

    public void updateCompass(float direction) {
        compass.update(direction);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permission is availble
            if (!googleApiClient.isConnected())
                googleApiClient.connect();
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation == null) {
                while (!googleApiClient.isConnected()) {
                }//wait until connection is complete
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }

            if (lastLocation != null) {
                handleNewLocation(lastLocation);
            } else {

            }
        } else {
            requestPermissionToLocation();
        }
    }

    public void requestPermissionToLocation() {
        View view = findViewById(R.id.mapFragment);
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(view, "Location access is required ",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(myMapFragment,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                }
            }).show();
        } else {
            Snackbar.make(view,
                    "Permission is not available. Requesting Location permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(myMapFragment, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(myMapFragment, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        lastLocation = location;
        double currentLatitude = lastLocation.getLatitude();
        double currentLongitude = lastLocation.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!").icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.myposition));
        if (map != null) {
            map.addMarker(options);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 12.0f));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

}
