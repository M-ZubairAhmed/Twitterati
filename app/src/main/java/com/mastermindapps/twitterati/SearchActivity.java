package com.mastermindapps.twitterati;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npE) {
            Log.e("Search-timelineAct", "Null pointer for toolbar displaying as up");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_search_xml);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query("#usa")
                .languageCode(Locale.ENGLISH.getLanguage())
                .maxItemsPerRequest(100)
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(searchTimeline)
                .build();

        TextView noResultsTextV = (TextView) findViewById(R.id.no_results_xml);
        ListView searchResultsListV = (ListView) findViewById(R.id.search_results_xml);

        searchResultsListV.setEmptyView(noResultsTextV);
        searchResultsListV.setAdapter(adapter);
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
//                swipeRefreshLayout.setRefreshing(true);
//                refreshAdapter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
