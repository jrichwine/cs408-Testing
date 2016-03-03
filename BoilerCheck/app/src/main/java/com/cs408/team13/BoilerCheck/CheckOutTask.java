package com.cs408.team13.BoilerCheck;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacob on 3/3/2016.
 */
public class CheckOutTask extends AsyncTask<Void, Void, Boolean>
{
    private final Context viewContext;
    private boolean result = false;

    CheckOutTask(Context context) {
        this.viewContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            BackEndRestClient.get("users/checkOut", null, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    Log.d("onSuccess", "StatusCode:" + statusCode);
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

        return result;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (success) {
            Toast.makeText(viewContext, "CheckOut Success:" + BoilerCheck.CurrentBuilding, Toast.LENGTH_SHORT).show();
            BoilerCheck.CurrentBuilding = null;
            result = true;
        } else {
            Toast.makeText(viewContext, "CheckOut Failure:" + BoilerCheck.CurrentBuilding, Toast.LENGTH_SHORT).show();
            result = false;
        }
    }

}