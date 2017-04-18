package com.mastermindapps.twitterati;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

class TwitterAdapter extends TweetTimelineListAdapter {

    private final String TAG = "TwitterAdapter";

    private Context context;
    private Timeline<Tweet> timeline;

    TwitterAdapter(Context context, Timeline<Tweet> timeline) {
        super(context, timeline);
        this.context = context;
        this.timeline = timeline;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
//            String g = getItemId(position);
            View view = super.getView(position, convertView, parent);
            view.setEnabled(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long tweetId = getItemId(position);
                    Intent intent = new Intent(context, TweetActivity.class);
                    intent.putExtra("tweetID", tweetId);
                    context.startActivity(intent);
                }
            });
            return view;
        } catch (NullPointerException npE) {
            long id = getItemId(position);
            Log.e(TAG, "Cannot include tweet: " + id + "\n" + npE);
            return null;
        }
    }
}
