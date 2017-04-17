package com.mastermindapps.twitterati;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    ListView searchResultsListV;
    ProgressBar loadingBar;

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

        TextView noResultsTextV = (TextView) findViewById(R.id.no_results_xml);
        loadingBar = (ProgressBar) findViewById(R.id.progress_bar_search_xml);
        searchResultsListV = (ListView) findViewById(R.id.search_results_xml);

        searchResultsListV.setEmptyView(noResultsTextV);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_search_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sendQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        switch (id) {
//            case :
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }

    void sendQuery(String queryString) {
        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(queryString)
                .languageCode(Locale.ENGLISH.getLanguage())
                .maxItemsPerRequest(100)
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(searchTimeline)
                .build();

        searchResultsListV.setAdapter(adapter);

    }

}
