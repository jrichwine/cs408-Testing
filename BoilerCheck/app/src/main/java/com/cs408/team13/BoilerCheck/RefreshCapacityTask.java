package com.cs408.team13.BoilerCheck;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.CaptioningManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacob on 3/3/2016.
 */
public class RefreshCapacityTask extends AsyncTask<Void, Void, Boolean>
{
    private final Context viewContext;
    private boolean result = false;
    RefreshCapacityTask(Context context) {
        this.viewContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            BackEndRestClient.get("users/refreshCapacity", null, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    Log.d("onSuccess", "StatusCode:" + statusCode);


                    try {
                        Gson gson = new Gson();
                        String dataResponse = "";
                        String formattedResponse = "";

                        dataResponse = new String(response, "UTF-8");
                        formattedResponse = "{ Caps:" + dataResponse + "}";
                        Log.d("Data returned", formattedResponse);

                        class Cap
                        {
                            public String _id;
                            public int CurrentCapacity;
                        }

                        class Caps
                        {
                            Cap[] Caps;
                        }

                        Caps c = new Caps();
                        c = gson.fromJson(formattedResponse, Caps.class);

                        //Log.d("Data returned", c);

                        onPostExecute(true);
                    } catch (Exception e) {
                        Log.d("ERROR", "Error parsing returned data");
                        onPostExecute(false);
                    }

                    onPostExecute(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.d("onError", "StatusCode:" + statusCode);
                    onPostExecute(false);
                }
            });
            Thread.sleep(200);
        } catch (InterruptedException e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (success) {
            Toast.makeText(viewContext, "Updated Capacities", Toast.LENGTH_SHORT).show();
            //result = true;
        } else {
            Toast.makeText(viewContext, "Failed Updated Capacities", Toast.LENGTH_SHORT).show();
            //result = false;
        }
    }

}