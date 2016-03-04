package com.cs408.team13.BoilerCheck;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacob on 3/3/2016.
 */
public class RefreshCapacityTask extends AsyncTask<Void, Void, String>
{
    private final Context viewContext;
    private boolean result = false;
    RefreshCapacityTask(Context context) {
        this.viewContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {

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

                        Caps c = gson.fromJson(formattedResponse, Caps.class);

                        for(int i = 0; i < c.Caps.size(); i++)
                        {
                            BoilerCheck.loadedBuildings.Buildings[i].CurrentCapacity = c.Caps.get(i).CurrentCapacity;
                        }

                        onPostExecute("0");
                    } catch (Exception e) {
                        Log.d("ERROR", "Error parsing returned data");
                        onPostExecute("1");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.d("onError", "StatusCode:" + statusCode);
                    onPostExecute("1");
                }
            });
            Thread.sleep(200);
        } catch (InterruptedException e) {
            return "1";
        }
        return "2";
    }

    @Override
    protected void onPostExecute(final String result) {

        switch(result)
        {
            case "0": Toast.makeText(viewContext, "Updated Capacities", Toast.LENGTH_SHORT).show();
                    break;
            case "1": Toast.makeText(viewContext, "Failed Updated Capacities", Toast.LENGTH_SHORT).show();
                    break;
            case "2":
                    //async task is complete, do something else, like refresh listview
                    break;
        }
    }
}