package com.mastermindapps.twitterati;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    String userHandleTwitter;
    String userNameTwitter;
    String userPicURLTwitter;
    String userCoverURLTwitter;
    ListView homeListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeListView = (ListView) findViewById(R.id.home_list_xml);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TwitterSession twitterSession = Twitter.getInstance().core.getSessionManager().getActiveSession();
        long userTwitterSessionID = twitterSession.getUserId();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TwitterSecret.TWITTER_KEY, TwitterSecret.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        if (userTwitterSessionID != 0) {
            StatusesService statusesService = Twitter.getApiClient().getStatusesService();
            Call<List<Tweet>> tweelListCall = statusesService.homeTimeline(100, null, null, false, false, false, false);
            tweelListCall.enqueue(new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> result) {
                    List<Tweet> tweets = result.data;
                    if (tweets != null) {
                        FixedTweetTimeline fixedTweetTimeline = new FixedTweetTimeline.Builder().setTweets(tweets).build();

                        TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(HomeActivity.this).setTimeline(fixedTweetTimeline).build();
                        homeListView.setAdapter(adapter);
                    }

                }

                @Override
                public void failure(TwitterException exception) {

                }
            });
        }


        TextView userNameTextV = (TextView) navigationView.getHeaderView(0).findViewById(R.id.twitter_user_name_xml);
        TextView userHandleTextV = (TextView) navigationView.getHeaderView(0).findViewById(R.id.twitter_user_handle_xml);
        ImageView userPicImageV = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.twitter_user_pic_xml);
        ImageView userCoverImageV = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.twitter_user_cover_xml);

        userNameTextV.setText(UserInfo.userName);
        userHandleTextV.setText(UserInfo.userHandle);
        try {
            userPicURLTwitter = UserInfo.userPicUrl;
            userCoverURLTwitter = UserInfo.userCoverUrl;
            Picasso.with(this).load(userPicURLTwitter.replace("_normal", "")).resize(150, 150).into(userPicImageV);
            Picasso.with(this).load(userCoverURLTwitter).fit().into(userCoverImageV);
        } catch (NullPointerException npE) {
            Log.e("Null pointer", "User profiles and cover url is absent");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_action_report:
                break;
            case R.id.menu_action_logout:
                logoutTwitter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent navIntent;
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_profile:
                break;
            case R.id.nav_timeline:
                navIntent = new Intent(HomeActivity.this, TimelineActivity.class);
                navIntent.putExtra("UserScreenName", userHandleTwitter);
                startActivityForResult(navIntent, 200);
                break;
            case R.id.nav_search:
                navIntent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(navIntent);
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_help:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void logoutTwitter() {
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        Intent gobackSignin = new Intent(HomeActivity.this, SigninActivity.class);
        startActivity(gobackSignin);
        finish();
        Toast.makeText(getApplicationContext(), "You are logged out", Toast.LENGTH_LONG).show();
    }


}

