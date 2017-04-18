package com.mastermindapps.twitterati;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetView;

import retrofit2.Call;

public class TweetActivity extends AppCompatActivity {

    private final String ACTIVITY_NAME = "TweetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        final long tweetID = bundle.getLong("tweetID");
        Log.d(ACTIVITY_NAME, "tweet ID recieved: " + tweetID);

        final CardView rootLayout = (CardView) findViewById(R.id.tweet_show_xml);

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<Tweet> call = statusesService.show(tweetID, false, true, true);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TweetView tweetView = new TweetView(TweetActivity.this, result.data, R.style.tw__TweetLightStyle);
                rootLayout.addView(tweetView);
                tweetView.setEnabled(false);
            }

            @Override
            public void failure(TwitterException twtE) {
                Log.d(ACTIVITY_NAME, "Rendering of tweet: " + tweetID + " failed. Stack trace\n" + twtE);
            }
        });
    }

}
