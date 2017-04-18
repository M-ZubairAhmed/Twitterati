package com.mastermindapps.twitterati;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TimelineActivity extends AppCompatActivity {

    final String TAG = "Timeline Activity";
    TwitterAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npE) {
            Log.e(TAG, "Null pointer for toolbar displaying as up");
        }
        Bundle bundle = getIntent().getExtras();

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(bundle.getString("UserScreenName"))
                .includeReplies(true)
                .includeRetweets(true)
                .build();

        adapter = new TwitterAdapter(TimelineActivity.this, userTimeline);

        TextView emptyTimeline = (TextView) findViewById(R.id.notimeline_xml);
        ListView listView = (ListView) findViewById(R.id.timeline_list);

        listView.setEmptyView(emptyTimeline);
        listView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                refreshAdapter();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subactivities_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                refreshAdapter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void refreshAdapter() {
        adapter.refresh(new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(TimelineActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
