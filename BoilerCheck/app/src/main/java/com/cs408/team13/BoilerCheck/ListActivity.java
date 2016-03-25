package com.cs408.team13.BoilerCheck;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class ListActivity extends AppCompatActivity {

    private ListView list;
    private logout mLogoutTask = null;
    private RefreshCapacityTask mRefreshCapacityTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        list = (ListView) findViewById(R.id.building_list);
        list.setAdapter(new BuildingListAdapter(this,getIntent().getIntExtra("filter",-1)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                attemptLogout();
                break;
            case R.id.action_refresh:
                attemptRefresh();
                break;
            default:
                break;
        }
        return true;
    }

    public void attemptLogout() {
        mLogoutTask = new logout();
        mLogoutTask.execute((Void) null);
    }

    public void attemptRefresh() {
        mRefreshCapacityTask = new RefreshCapacityTask(ListActivity.this);
        mRefreshCapacityTask.execute((Void) null);
    }

    public class logout extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... params) {

            try {

                RequestParams rparams = new RequestParams();

                BoilerCheck.RestClient.post("/users/logout", rparams, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

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
                    SaveSharedPreference.clear(ListActivity.this);
                    Intent i = new Intent(ListActivity.this, LoginActivity.class);
                    startActivity(i);
                    break;
                case "1":
                    break;
                case "2":
                    break;


            }
        }
    }
}
