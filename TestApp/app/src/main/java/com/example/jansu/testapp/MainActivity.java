package com.example.jansu.testapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient client;
    private Location lastLocation;
    private TextView latitudeText;
    private TextView longitudeText;
    private LocationRequest locationRequest;
    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button b = (Button) findViewById(R.id.button_id);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {


            }


        });

        latitudeText = (TextView) findViewById(R.id.latitude);
        longitudeText = (TextView) findViewById(R.id.longitude);

        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(1000);
    }


    protected void onStart() {
        client.connect();
        super.onStart();
    }

    protected void onResume() {
        if(checkGooglePlayServices()) {
            client.connect();
        }
        super.onResume();
    }

    protected void onPause() {
        stopLocationUpdates();
        super.onPause();
    }

    protected  void stopLocationUpdates(){
        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);

        }
    }

    protected void onStop() {
        if (client != null)
        client.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle conn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
                if (lastLocation != null) {
                    latitudeText.setText(String.valueOf(lastLocation.getLatitude()));
                    longitudeText.setText(String.valueOf(lastLocation.getLongitude()));
                    Log.i("onConnected", "Location services is connected");
                }else {
                    LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, (LocationListener) this);

                }


            }
        }
        else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
                if (lastLocation != null) {
                    latitudeText.setText(String.valueOf(lastLocation.getLatitude()));
                    longitudeText.setText(String.valueOf(lastLocation.getLongitude()));

                }
                Log.i("OnConnected", "Location services is connected");

            }


        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("OnSuspended", "Location services is suspended, please reconnect!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("OnConFailed", "Location services is failed" + connectionResult.toString() + connectionResult.getErrorCode() + connectionResult.getErrorMessage());
        Toast.makeText(MainActivity.this, "Failed:" + connectionResult.getErrorMessage() + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        latitudeText.setText(String.valueOf(location.getLatitude()));
        longitudeText.setText(String.valueOf(location.getLongitude()));
        Toast.makeText(MainActivity.this, "Location has changed", Toast.LENGTH_LONG);
    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleAvail = GoogleApiAvailability.getInstance();
        int status = googleAvail.isGooglePlayServicesAvailable(this);
        if(status != ConnectionResult.SUCCESS) {
            googleAvail.getErrorDialog(this, status, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
            return false;
        }

        return true;
    }
}
