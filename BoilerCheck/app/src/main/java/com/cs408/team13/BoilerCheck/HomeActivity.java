package com.cs408.team13.BoilerCheck;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class HomeActivity extends AppCompatActivity  {

    private TextView mTextView;
    private Button mCheckoutButton;
    private CheckOutTask mCheckoutTask = null;
    private logout mLogoutTask = null;
    private UserLoginTask mLoginTask = null;
    private Button mEatButton;
    private Button mStudyButton;
    private Button mPlayButton;
    private GetBuildingData mAuthTask = null;
    private RefreshCapacityTask mRefreshCapacityTask = null;

    private final static int perms = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    perms);

        }
        //

//        SaveSharedPreference.clear(HomeActivity.this);
        if(SaveSharedPreference.getUserName(HomeActivity.this).length() == 0) {
            Intent i = new Intent(this, LoginActivity.class);
            finish();
            startActivity(i);
        }
        else {
            mLoginTask = new UserLoginTask(SaveSharedPreference.getUserName(HomeActivity.this), SaveSharedPreference.getPassword(HomeActivity.this));
            mLoginTask.execute((Void) null);
        }



        mCheckoutButton = (Button) findViewById(R.id.action_checkout);
        mTextView = (TextView) findViewById(R.id.textView_home);
        mEatButton = (Button) findViewById(R.id.button_eat);
        mStudyButton = (Button) findViewById(R.id.button_work);
        mPlayButton = (Button) findViewById(R.id.button_play);
        mEatButton.setTag(R.string.eat_filter);
        mStudyButton.setTag(R.string.work_filter);
        mPlayButton.setTag(R.string.play_filter);

        if (BoilerCheck.CurrentBuilding == null) {
            mTextView.setTextColor(Color.RED);
            mTextView.setText("Not Checked In");
        }
        else {
            mTextView.setTextColor(Color.WHITE);
            mTextView.setText("Checked in to: " + BoilerCheck.CurrentBuilding);
            mCheckoutButton.setEnabled(true);
            mCheckoutButton.setVisibility(View.VISIBLE);
        }

//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getData();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case perms: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    BoilerCheck.locationService = new LocationService(BoilerCheck.me);
                    BoilerCheck.locationService.connect();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }









    @Override
    public void onResume() {
        super.onResume();

        if (BoilerCheck.CurrentBuilding == null) {
            mTextView.setTextColor(Color.RED);
            mTextView.setText("Not Checked In");
        }
        else {
            mTextView.setTextColor(Color.WHITE);
            mTextView.setText("Checked in to: " + BoilerCheck.CurrentBuilding);
            mCheckoutButton.setEnabled(true);
            mCheckoutButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        refresh.setVisible(false);
        refresh.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                attemptLogout();
                break;
            default:
                break;
        }
        return true;
    }

    public void viewList(View v) {
        Intent i = new Intent(this, ListActivity.class).putExtra("filter", (int)v.getTag());
        startActivity(i);
    }

    public void getData() {

        mAuthTask = new GetBuildingData();
        mAuthTask.execute((Void) null);

    }

    public void attemptLogout() {
        mLogoutTask = new logout();
        mLogoutTask.execute((Void) null);
    }

    public void attemptCheckout(View v) {
        mCheckoutTask = new CheckOutTask(HomeActivity.this);
        mCheckoutTask.execute((Void) null);
        mTextView.setTextColor(Color.RED);
        mTextView.setText("Not Checked In");
        mCheckoutButton.setEnabled(false);
        mCheckoutButton.setVisibility(View.INVISIBLE);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            //this.context = context.getApplicationContext();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                RequestParams rparams = new RequestParams();
                rparams.put("email", mEmail);
                rparams.put("password", mPassword);

                BoilerCheck.RestClient.post("login", rparams, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    @Override
                    public void onStart() {
                        // called before request is started
                        Log.d("onSending", "Username:" + mEmail + " Password:" + mPassword);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"

                        Log.d("onSuccess", "StatusCode:" + statusCode);
                        onPostExecute("0");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d("onFailure", "StatusCode:" + statusCode);
                        onPostExecute("1");

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return "1";
            }

            return "2";
        }

        @Override
        protected void onPostExecute(final String result) {
            mLoginTask = null;

            switch(result)
            {
                case "0": //Success
                    break;
                case "1":   //Failure
                    break;
                case "2":   //task complete
                    break;


            }
//                SaveSharedPreference.setUserName(LoginActivity.this, mEmail);
//                finish();
                //Load next page
//                Intent intent_name = new Intent();
//                intent_name.setClass(getApplicationContext(), HomeActivity.class);
//                startActivity(intent_name);

        }

        @Override
        protected void onCancelled() {
            mLoginTask = null;
        }
    }

    public class logout extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... params) {

            try {

                RequestParams rparams = new RequestParams();

                BoilerCheck.RestClient.post("/logout", rparams, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    @Override
                    public void onStart() {
                        // called before request is started
                        List<Cookie> cookies = BoilerCheck.myCookieStore.getCookies();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        Log.d("onSuccess", "StatusCode:" + statusCode);
                        onPostExecute("0");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d("onFailure", "StatusCode:" + statusCode);

                        onPostExecute("1");

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
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
            mLogoutTask = null;

            switch(result)
            {
                case "0":
                    Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(i);
                    break;
                case "1":
                    break;
                case "2":
                    break;


            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public class GetBuildingData extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... params) {
            try {

                BoilerCheck.RestClient.get("users/getBuildings", null, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"

                        Log.d("onSuccess", "StatusCode:" + statusCode);

                        try {
                            Gson gson = new Gson();
                            String dataResponse = "";
                            String formattedResponse = "";

                            dataResponse = new String(response, "UTF-8");
                            formattedResponse = "{ Buildings:" + dataResponse + "}";
                            Log.d("Data returned", formattedResponse);

                            BoilerCheck.loadedBuildings = gson.fromJson(formattedResponse, Buildings.class);
                            BoilerCheck.loadedBuildings.distanceSort();
                            BoilerCheck.loadedBuildings.nearestBuilding();

                            onPostExecute("0");
                        } catch (Exception e) {
                            Log.d("ERROR", "Error parsing returned data");
                            onPostExecute("1");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d("onFailure", "StatusCode:" + statusCode);

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
        protected void onPostExecute(final String success) {
            mAuthTask = null;

            switch (success) {

                case "0":
                    Toast.makeText(mTextView.getContext(), "Data Retrieved", Toast.LENGTH_SHORT).show();
                    break;
                case "1":
                    Toast.makeText(mTextView.getContext(), "Failure Retrieving Data", Toast.LENGTH_SHORT).show();
                    break;
                case "2":
                    Toast.makeText(mTextView.getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}