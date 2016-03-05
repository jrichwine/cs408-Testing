package com.cs408.team13.BoilerCheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class HomeActivity extends AppCompatActivity  {

    private TextView mTextView;
    private logout mLogoutTask = null;
    private UserLoginTask mLoginTask = null;
    private Button mEatButton;
    private Button mStudyButton;
    private Button mPlayButton;
    private GetBuildingData mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        SaveSharedPreference.clear(HomeActivity.this);
        if(SaveSharedPreference.getUserName(HomeActivity.this).length() == 0) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        else {
            mLoginTask = new UserLoginTask(SaveSharedPreference.getUserName(HomeActivity.this), SaveSharedPreference.getPassword(HomeActivity.this));
            mLoginTask.execute((Void) null);
        }


        mTextView = (TextView) findViewById(R.id.textView_home);
        mEatButton = (Button) findViewById(R.id.button_eat);
        mStudyButton = (Button) findViewById(R.id.button_work);
        mPlayButton = (Button) findViewById(R.id.button_play);
        mEatButton.setTag(R.string.eat_filter);
        mStudyButton.setTag(R.string.work_filter);
        mPlayButton.setTag(R.string.play_filter);


        getData();
    }

    public void viewList(View v) {
        Intent i = new Intent(this, ListActivity.class).putExtra("filter", (int)v.getTag());
        startActivity(i);
    }

    public void getData() {

        mAuthTask = new GetBuildingData();
        mAuthTask.execute((Void) null);

    }

    public void attemptLogout(View v) {
        mLogoutTask = new logout();
        mLogoutTask.execute((Void) null);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            //this.context = context.getApplicationContext();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
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
                        if(headers != null) {
                            for (Header head : headers) {
                                Log.d("Headers", head.getName() + ":" + head.getValue());
                            }

                            for (Cookie c : BoilerCheck.myCookieStore.getCookies()) {
                                Log.d("Cookies", c.getName() + c.getValue());
                            }
                        }
                        onPostExecute(true);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d("onFailure", "StatusCode:" + statusCode);

                        if(headers != null)
                        {

                            for (Header head : headers) {
                                Log.d("Headers", head.getName() + ":" + head.getValue());
                            }

                            for (Cookie c : BoilerCheck.myCookieStore.getCookies()) {
                                Log.d("Cookies", c.getName() + c.getValue());
                            }
                        }
                        onPostExecute(false);

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLoginTask = null;

            if (success) {
//                SaveSharedPreference.setUserName(LoginActivity.this, mEmail);
//                finish();
                //Load next page
//                Intent intent_name = new Intent();
//                intent_name.setClass(getApplicationContext(), HomeActivity.class);
//                startActivity(intent_name);
            }
        }

        @Override
        protected void onCancelled() {
            mLoginTask = null;
        }
    }

    public class logout extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {

                RequestParams rparams = new RequestParams();

                BoilerCheck.RestClient.post("/users/logout", rparams, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    @Override
                    public void onStart() {
                        // called before request is started
                        List<Cookie> cookies = BoilerCheck.myCookieStore.getCookies();
                        for (int i = 0; i < cookies.size(); i++) {
                            Log.d("Saved Cookies: ", ":" + cookies.get(i));
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"

                        Log.d("onSuccess", "StatusCode:" + statusCode);
                        if (headers != null) {
                            for (Header head : headers) {
                                Log.d("Headers", head.getName() + ":" + head.getValue());
                            }

                            for (Cookie c : BoilerCheck.myCookieStore.getCookies()) {
                                Log.d("Cookies", c.getName() + c.getValue());
                            }
                        }
                        onPostExecute(true);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d("onFailure", "StatusCode:" + statusCode);

                        if (headers != null) {

                            for (Header head : headers) {
                                Log.d("Headers", head.getName() + ":" + head.getValue());
                            }

                            for (Cookie c : BoilerCheck.myCookieStore.getCookies()) {
                                Log.d("Cookies", c.getName() + c.getValue());
                            }
                        }
                        onPostExecute(false);

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLogoutTask = null;

            if (success) {
//                mTextView.setText("logged out");
                //finish();
                SaveSharedPreference.clear(HomeActivity.this);
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public class GetBuildingData extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... params) {
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

                            onPostExecute(true);
                        } catch (Exception e) {
                            Log.d("ERROR", "Error parsing returned data");
                            onPostExecute(false);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d("onFailure", "StatusCode:" + statusCode);

                        onPostExecute(false);
                    }
                });
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                Toast.makeText(mTextView.getContext(), "Data Retrieved", Toast.LENGTH_SHORT).show();
                //finish();
            } else {
                Toast.makeText(mTextView.getContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}