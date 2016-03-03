package com.cs408.team13.BoilerCheck;
import android.os.Looper;
import android.util.Log;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * Created by Jacob on 2/6/2016.
 */
public class BackEndRestClient {

        //Address of local dev machine
        private static final String BASE_URL = "http://10.0.2.2:3000/";

        private static AsyncHttpClient client = new AsyncHttpClient();

        private static PersistentCookieStore myCookieStore;


        public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.get(getAbsoluteUrl(url), params, responseHandler);
        }

        public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.post(getAbsoluteUrl(url), params, responseHandler);
        }

        public static void setCookieStore(PersistentCookieStore cookie)
        {
            myCookieStore = cookie;
        }

        private static String getAbsoluteUrl(String relativeUrl) {
            return BASE_URL + relativeUrl;
        }

        //Rework these....
        public static void CheckIn(String building, AsyncHttpResponseHandler asyncHttpResponseHandler)
        {

            RequestParams rparams = new RequestParams();
            rparams.put("building", building);

            BoilerCheck.RestClient.post("users/checkin", rparams, asyncHttpResponseHandler);
        }

        public static void CheckOut(AsyncHttpResponseHandler asyncHttpResponseHandler)
        {

        BoilerCheck.RestClient.get("users/checkOut", null, asyncHttpResponseHandler);
        }

        public static void RefreshCapacity(AsyncHttpResponseHandler asyncHttpResponseHandler)
        {

        BoilerCheck.RestClient.get("users/refreshCapacity", null, asyncHttpResponseHandler);
            //Put return data into BoilerCHeck.buildings
        }

}
