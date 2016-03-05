package com.cs408.team13.BoilerCheck;

import android.app.Application;

import com.loopj.android.http.PersistentCookieStore;

/**
 * Created by Jacob on 2/10/2016.
 */
public class BoilerCheck extends Application {

    //RESTClient
    public static BackEndRestClient RestClient;
    public static PersistentCookieStore myCookieStore;
    public static LocationService locationService;
    public static Buildings loadedBuildings;
    public static String CurrentBuilding = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        RestClient = new BackEndRestClient();
        myCookieStore = new PersistentCookieStore(this);
        RestClient.setCookieStore(myCookieStore);
        locationService = new LocationService(this);
    }
}
