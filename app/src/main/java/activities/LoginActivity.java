package activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import db.DBOperator;
import fragments.LoginFragment;

/**
 * Created by אילון on 26/11/2016.
 */

public class LoginActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static DBOperator database;
    public static Location lastLocation;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private LoginFragment loginFragment;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LoginActivity", "onCreate:");
        setContentView(R.layout.activity_main);
        database = new DBOperator(this);//sets the connection to the sqllite db
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        googleApiClient.connect();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        View view = findViewById(R.id.container);
        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(view, "Location permission was granted. Connecting...",
                        Snackbar.LENGTH_LONG)
                        .show();
                onConnected(null);
            } else {
                Snackbar.make(view, "Location permission request was denied.",
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permission is availble
            if (!googleApiClient.isConnected())
                googleApiClient.connect();
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation == null) {
                while (!googleApiClient.isConnected()) {
                }//wait until connection is complete
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } else {
                Toast.makeText(this, "Location saved! Lat=" +  lastLocation.getLatitude() + " long=" +  lastLocation.getLongitude(), Toast.LENGTH_LONG);
            }

            if ( lastLocation != null) {
                locationGranted();
            }
        } else {
            requestPermissionToLocation();
        }
    }

    public void requestPermissionToLocation() {
        View view = findViewById(R.id.endLayout);
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(view, "Location access is required ",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                }
            }).show();
        } else {
            Snackbar.make(view,
                    "Permission is not available. Requesting Location permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
    }
    public void locationGranted() {
        if ( lastLocation != null) {
            loginFragment = new LoginFragment();
            getFragmentManager().beginTransaction().add(R.id.main_fragment, loginFragment).commit();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        Log.d("Location: ", location.toString());
        lastLocation = location;
        if (lastLocation != null)
            locationGranted();
    }
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public void startGame(int level, String name) {
        Intent intent = new Intent(getApplicationContext(), GamePlayActivity.class);
        intent.putExtra("level", level);
        intent.putExtra("name", name);
        intent.putExtra("lat",lastLocation.getLatitude());
        intent.putExtra("long",lastLocation.getLongitude());
        startActivity(intent);
    }
}
