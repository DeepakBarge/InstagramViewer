package com.example.deepak.instagramviewer;

import android.app.Application;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ListView lvFeeds = (ListView) findViewById(R.id.lvFeedList);

        feedItems = new ArrayList<>();
        adapter = new FeedItemAdapter(this, feedItems);
        lvFeeds.setAdapter(adapter);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchFeedData();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchFeedData();
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
                //data=>images=>standard resolution=>url
                //data=>likes=>count
                //data=>comments=>count
                //data=>comments=>data=>text
                //data=>comments=>data=>from=>username
                //data=>comments=>data=>from=>profile_picture

                // clear up the feedItems
                feedItems.clear();

                Log.i("info",response.toString());

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
                        fItem.caption = feedJSON.getJSONObject("caption").getString("text");
                        //fItem.mediaType = feedJSON.getString("type");
                        //if(fItem.mediaType.equalsIgnoreCase("video")){
                        //    fItem.videoUrl = feedJSON.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
                        //}
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
