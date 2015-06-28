package com.example.hazai.supported;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import io.fabric.sdk.android.Fabric;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.CollectionTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUi;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import java.util.Arrays;
import java.util.List;

public class TwitterPage extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TwitterAuthConfig authConfig =  new TwitterAuthConfig("rUV0dbllx6rk9VJF5SJ7e7n3c", "AgtQTYjY2JFomShi5kHpzFYxJCbApZaREO0N62t3jCZIs9CZL6");
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_twitter_page);
//        final LinearLayout myLayout
//                = (LinearLayout) findViewById(R.id.tweets);
//
//        final List<Long> tweetIds =
//                Arrays.asList(1275302468L, 200L);
//
//        TweetUtils.loadTweets(tweetIds, new LoadCallback<List<Tweet>>() {
//            @Override
//            public void success(List<Tweet> tweets) {
//                for (Tweet tweet : tweets) {
//                    Log.v("tweet", tweet.toString());
//                    myLayout.addView(new CompactTweetView(TwitterPage.this, tweet));
//                }
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.v("hi", exception.getMessage());
//            }
//        });
        final CollectionTimeline timeline = new CollectionTimeline.Builder()
                .id(615138062650208257L)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, timeline);
        setListAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
