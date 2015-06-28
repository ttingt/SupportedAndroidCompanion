package com.example.hazai.supported;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.CollectionTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;


public class TwitterPage extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TwitterAuthConfig authConfig =  new TwitterAuthConfig("rUV0dbllx6rk9VJF5SJ7e7n3c", "AgtQTYjY2JFomShi5kHpzFYxJCbApZaREO0N62t3jCZIs9CZL6");
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_twitter_page);
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
