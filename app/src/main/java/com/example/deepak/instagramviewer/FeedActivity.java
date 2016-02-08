package com.example.deepak.instagramviewer;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class FeedActivity extends AppCompatActivity {

    private static final String API_ENDPOINT = "https://api.instagram.com/v1/media/popular?client_id=e05c462ebd86446ea48a5af73769b602";

    ArrayList<FeedItem> feedItems;
    FeedItemAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    ListView lvFeeds;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_appicon);
        getSupportActionBar().setTitle("InstaPicViewer");

        lvFeeds = (ListView) findViewById(R.id.lvFeedList);

        feedItems = new ArrayList<>();
        adapter = new FeedItemAdapter(this, feedItems);
        lvFeeds.setAdapter(adapter);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                fetchFeedData();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchFeedData();

        setupItemClickListenerForVideo();

    }

    private String getRelativeTimestampString(String createdTime){
        Long postedTime = Long.valueOf(createdTime) * 1000;
        Long now = System.currentTimeMillis();
        String temp = DateUtils.getRelativeTimeSpanString(postedTime, now, DateUtils.MINUTE_IN_MILLIS).toString();
        return temp;
    }

    private void setupItemClickListenerForVideo(){
        lvFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(feedItems.get(position).mediaType == FeedItem.VIDEO) {

                    Intent i = new Intent(getApplicationContext(), VideoPlayActivity.class);
                    i.putExtra("url", feedItems.get(position).videoUrl);
                    startActivity(i);
                }
            }
        });

    }

    private void fetchFeedData(){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_ENDPOINT,  new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //data=>user=>username
                //data=>user=>profile_picture
                //data=>caption=>text
                //data=>type=>image/video
                //data=>created_time
                //data=>images=>standard resolution=>url
                //data=>likes=>count
                //data=>comments=>count
                //data=>comments=>data=>text
                //data=>comments=>data=>from=>username
                //data=>comments=>data=>from=>profile_picture

                // clear up the feedItems
                feedItems.clear();

                JSONArray feedsJSON = null;

                try{
                    feedsJSON = response.getJSONArray("data");

                    for(int i=0; i<feedsJSON.length(); i++){

                        JSONObject feedJSON = feedsJSON.getJSONObject(i);
                        FeedItem fItem = new FeedItem();
                        fItem.username = feedJSON.getJSONObject("user").getString("username");
                        fItem.likeCount = feedJSON.getJSONObject("likes").getInt("count");
                        fItem.imageUrl = feedJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        fItem.profilePictureUrl = feedJSON.getJSONObject("user").getString("profile_picture");
                        if(feedJSON.optJSONObject("caption") != null){
                            fItem.caption = feedJSON.getJSONObject("caption").getString("text");
                        }else{
                            fItem.caption = "";
                        }
                        fItem.createdTime = feedJSON.getString("created_time");
                        fItem.relativeTimestampString = getRelativeTimestampString(fItem.createdTime);

                        int commentCount = feedJSON.getJSONObject("comments").getInt("count");

                        JSONArray commentsData = feedJSON.getJSONObject("comments").getJSONArray("data");

                        for(int j=0; j<commentsData.length(); j++){
                            JSONObject commentData = commentsData.getJSONObject(j);
                            Comment c = new Comment();
                            c.username = commentData.getJSONObject("from").getString("username");
                            c.commentText = commentData.getString("text");
                            c.profilePictureUrl = commentData.getJSONObject("from").getString("profile_picture");
                            fItem.comments.add(c);
                        }

                        if(feedJSON.optJSONObject("videos") != null){
                            fItem.videoUrl = feedJSON.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
                            fItem.mediaType = FeedItem.VIDEO;
                        }else{
                            fItem.videoUrl = "";
                            fItem.mediaType = FeedItem.IMAGE;
                        }
                        feedItems.add(fItem);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //do error handling
            }
        });

    }
}
