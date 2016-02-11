package com.cs408.team13.BoilerCheck;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class HomeActivity extends AppCompatActivity  {

    private TextView mTextView;
    private GetTestData mAuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextView = (TextView) findViewById(R.id.textView_home);
        mTextView.setText("hello");
        getData();
    }
    public void getData() {

            mAuthTask = new GetTestData();
            mAuthTask.execute((Void) null);
        }



    public class GetTestData extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {

                RequestParams rparams = new RequestParams();

                BoilerCheck.RestClient.post("users/checkin", rparams, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    @Override
                    public void onStart() {
                        // called before request is started
                        List<Cookie> cookies = BoilerCheck.myCookieStore.getCookies();
                        for(int i = 0; i< cookies.size(); i++)
                        {
                            Log.d("Saved Cookies: ", ":" + cookies.get(i));
                        }
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
            mAuthTask = null;

            if (success) {
                mTextView.setText("AUTHED");
                //finish();
            } else {
                mTextView.setText("DONE");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}