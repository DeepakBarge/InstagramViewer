package com.example.deepak.instagramviewer;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by deepak on 2/3/16.
 */
public class FeedItemAdapter extends ArrayAdapter<FeedItem> {

    static Transformation transformation = new RoundedTransformationBuilder()
            .borderColor(Color.GRAY)
            .borderWidthDp(1)
            .cornerRadiusDp(30)
            .oval(false)
            .build();

    public FeedItemAdapter(Context context, ArrayList<FeedItem> feedItems) {
        super(context, 0, feedItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FeedItem fItem = getItem(position);

        if(convertView == null){
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.feed_item, parent, false);
            /*if(fItem.mediaType.equalsIgnoreCase("image")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.temp_item, parent, false);

                ImageView feedImage = (ImageView) convertView.findViewById(R.id.feedImage);

                feedImage.setImageResource(0);

                Picasso.with(getContext())
                        .load(fItem.imageUrl)
                        .into(feedImage);

            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.temp_item_video, parent, false);

                final VideoView mVideoView = (VideoView) convertView.findViewById(R.id.videoView);
                mVideoView.setVideoPath(fItem.videoUrl);
                MediaController mediaController = new MediaController(getContext());
                mediaController.setAnchorView(mVideoView);
                mVideoView.setMediaController(mediaController);
                mVideoView.requestFocus();
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        mVideoView.start();
                    }
                });

            }*/
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.temp_item, parent, false);
        }

        TextView username = (TextView) convertView.findViewById(R.id.username);
        username.setText(fItem.username);

        TextView caption = (TextView) convertView.findViewById(R.id.caption);
        caption.setText(fItem.caption);

        TextView likeCount = (TextView) convertView.findViewById(R.id.likeCount);
        likeCount.setText(String.valueOf(fItem.likeCount));

        ImageView feedImage = (ImageView) convertView.findViewById(R.id.feedImage);

        feedImage.setImageResource(0);

        Picasso.with(getContext())
                .load(fItem.imageUrl)
                .into(feedImage);

        ImageView profileImage = (ImageView) convertView.findViewById(R.id.profileImage);

        profileImage.setImageResource(0);

        //set placeholder images
        Picasso.with(getContext())
                .load(fItem.profilePictureUrl)
                .transform(transformation)
                .into(profileImage);

        return convertView;
    }
}
