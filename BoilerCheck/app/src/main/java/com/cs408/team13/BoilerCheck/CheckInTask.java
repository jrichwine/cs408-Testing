package com.cs408.team13.BoilerCheck;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacob on 3/3/2016.
 */
public class CheckInTask extends AsyncTask<Void, Void, String>
{
    private final String building;
    private final Context viewContext;
    private boolean result = false;

    CheckInTask(String building, Context context) {
        this.building = building;
        this.viewContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            RequestParams rparams = new RequestParams();
            rparams.put("building", building);

            BackEndRestClient.post("users/checkin", rparams, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    Log.d("onSuccess", "StatusCode:" + statusCode);
                    onPostExecute("0");
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
            case "0": Toast.makeText(viewContext, "CheckIn Success:" + building, Toast.LENGTH_SHORT).show();
                BoilerCheck.CurrentBuilding = building;
                break;
            case "1": Toast.makeText(viewContext, "CheckIn Failure:" + building, Toast.LENGTH_SHORT).show();
                break;
            case "2":
                //async task is complete, do something else
                break;
        }
    }
}