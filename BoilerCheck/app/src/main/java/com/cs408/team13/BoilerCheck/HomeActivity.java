package com.cs408.team13.BoilerCheck;

import android.content.Intent;
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
    private Button mEatButton;
    private Button mStudyButton;
    private Button mPlayButton;
    private GetBuildingData mAuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                            BoilerCheck.loadedBuildings.distanceSort();

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