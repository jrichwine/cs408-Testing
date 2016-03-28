package com.cs408.team13.BoilerCheck;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.location.LocationManager;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient client;
    private Location lastLocation;
    private int latitude;
    private int longitude;
    private LocationRequest locationRequest;
    private Context context;
    private CheckOutTask mCheckOutTask = null;
    private RefreshCapacityTask mRefreshCapacityTask = null;
    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;


    public LocationService(Context appContext) {
        context = appContext;

        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(1000)
                .setSmallestDisplacement(5);

    }

    public synchronized void buildGoogleApiClient() {
            client = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        /*if(checkGooglePlayServices()) {
            client.connect();
        }*/
    }

    public void connect() {
        client.connect();
    }
    public GoogleApiClient getClient() {
        return client;
    }

    public double calculateDistance(int lat1, int long1, int lat2, int long2) {

        Location locA = new Location("locA");
        locA.setLatitude(lat1);
        locA.setLongitude(long1);
        Location locB = new Location("locB");
        locB.setLatitude(lat2);
        locB.setLongitude(long2);

        double distance  = locA.distanceTo(locB);
        Log.i("Distance in meters: ", distance + "");
        return distance;
    }

    protected void onResume() {
        if(checkGooglePlayServices()) {
            client.connect();
        }
    }

    protected void onPause() {
        stopLocationUpdates();
    }

    protected  void stopLocationUpdates(){
        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);

        }
    }

    protected void onStop() {
        if (client != null)
            client.disconnect();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    @Override
    public void onConnected(Bundle conn) {

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
                if (lastLocation != null) {
                    latitudeText.setText(String.valueOf(lastLocation.getLatitude()));
                    longitudeText.setText(String.valueOf(lastLocation.getLongitude()));
                    Log.i("onConnected", "Location services is connected");
                }else {
                    LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest,  this);

                }


            }
        }
        else {*/
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest,  this);
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
            if (lastLocation != null) {
                latitude = (int)lastLocation.getLatitude();
                longitude = (int)lastLocation.getLongitude();
            }
            Log.i("OnConnected", "Location services is connected" + " " + latitude + " " + longitude);
        }



        //}
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("OnSuspended", "Location services is suspended, please reconnect!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("OnConFailed", "Location services is failed" + connectionResult.toString() + connectionResult.getErrorCode() + connectionResult.getErrorMessage());
        //Toast.makeText(this, "Failed:" + connectionResult.getErrorMessage() + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        latitude = (int)location.getLatitude();
        longitude = (int)location.getLongitude();
        Toast.makeText(context, "Location has changed", Toast.LENGTH_LONG);
    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleAvail = GoogleApiAvailability.getInstance();
        int status = googleAvail.isGooglePlayServicesAvailable(context);
        if(status != ConnectionResult.SUCCESS) {
            googleAvail.getErrorDialog((Activity)context, status, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
            return false;
        }

        return true;
    }
}
