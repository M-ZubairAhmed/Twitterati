package com.mastermindapps.twitterati;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_timeline);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npE) {
            Log.e("Toolbar-timelineAct", "Null pointer for toolbar displaying as up");
        }

        Bundle bundle = getIntent().getExtras();
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(bundle.getString("UserScreenName"))
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        TextView emptyTimeline = (TextView) findViewById(R.id.notimeline_xml);
        ListView listView = (ListView) findViewById(R.id.timeline_list);
        listView.setEmptyView(emptyTimeline);
        listView.setAdapter(adapter);

    }
}
