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
import android.widget.TextView;
import android.widget.Toast;

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
//        handleIntent(getIntent());

//        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
//                .query("#usa")
//                .languageCode(Locale.ENGLISH.getLanguage())
//                .maxItemsPerRequest(100)
//                .build();
//
//        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
//                .setTimeline(searchTimeline)
//                .build();

        TextView noResultsTextV = (TextView) findViewById(R.id.no_results_xml);
        ListView searchResultsListV = (ListView) findViewById(R.id.search_results_xml);

        searchResultsListV.setEmptyView(noResultsTextV);
//        searchResultsListV.setAdapter(adapter);
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
                Toast.makeText(SearchActivity.this, query, Toast.LENGTH_LONG).show();
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
        switch (id) {
            case R.id.menu_refresh:
//                swipeRefreshLayout.setRefreshing(true);
//                refreshAdapter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
