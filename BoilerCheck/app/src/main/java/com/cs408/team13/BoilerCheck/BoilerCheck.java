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

    public static Buildings loadedBuildings;

    @Override
    public void onCreate()
    {
        super.onCreate();
        RestClient = new BackEndRestClient();
        myCookieStore = new PersistentCookieStore(this);
        RestClient.setCookieStore(myCookieStore);
    }
}
