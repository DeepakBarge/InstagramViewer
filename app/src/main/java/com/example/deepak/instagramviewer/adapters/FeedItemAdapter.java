package com.example.deepak.instagramviewer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.deepak.instagramviewer.models.Comment;
import com.example.deepak.instagramviewer.models.FeedItem;
import com.example.deepak.instagramviewer.R;
import com.example.deepak.instagramviewer.activities.ViewAllCommentsActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedItemAdapter extends ArrayAdapter<FeedItem> {

    private Context context;
    static class FeedItemViewHolder {

        @Bind(R.id.username) TextView username;
        @Bind(R.id.caption) TextView caption;
        @Bind(R.id.likeCount) TextView likeCount;
        @Bind(R.id.feedImage) ImageView feedImage;
        @Bind(R.id.profileImage) ImageView profileImage;
        @Bind(R.id.playImage) ImageView playImage;
        @Bind(R.id.timestamp) TextView relativeTimestamp;
        @Bind(R.id.viewAllComments) TextView viewAllComments;
        @Bind(R.id.commentsLayout) LinearLayout commentList;

        public FeedItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    static Transformation transformation = new RoundedTransformationBuilder()
            .borderColor(Color.GRAY)
            .borderWidthDp(1)
            .cornerRadiusDp(30)
            .oval(false)
            .build();

    public FeedItemAdapter(Context context, ArrayList<FeedItem> feedItems) {
        super(context, 0, feedItems);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final FeedItem fItem = getItem(position);

        FeedItemViewHolder feedItemViewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.temp_item, parent, false);
            feedItemViewHolder = new FeedItemViewHolder(convertView);
            convertView.setTag(feedItemViewHolder);

        } else {
            feedItemViewHolder = (FeedItemViewHolder) convertView.getTag();
        }

        feedItemViewHolder.viewAllComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewAllCommentsActivity.class);
                i.putExtra("item", fItem);
                context.startActivity(i);
            }
        });

        feedItemViewHolder.username.setText(fItem.username);

        feedItemViewHolder.caption.setText(fItem.caption);

        feedItemViewHolder.likeCount.setText(String.valueOf(fItem.likeCount));

        feedItemViewHolder.feedImage.setImageResource(0);

        Picasso.with(getContext())
                .load(fItem.imageUrl)
                .into(feedItemViewHolder.feedImage);

        feedItemViewHolder.profileImage.setImageResource(0);

        Picasso.with(getContext())
                .load(fItem.profilePictureUrl)
                .transform(transformation)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(feedItemViewHolder.profileImage);

        feedItemViewHolder.relativeTimestamp.setText(fItem.relativeTimestampString);

        if(fItem.mediaType == FeedItem.IMAGE){
            feedItemViewHolder.playImage.setVisibility(View.INVISIBLE);
        } else {
            feedItemViewHolder.playImage.setVisibility(View.VISIBLE);
        }

        //LinearLayout commentList = (LinearLayout) convertView.findViewById(R.id.commentsLayout);
        feedItemViewHolder.commentList.removeAllViews();

        //display only top 2 comments
        for(int i=0; i<2 && fItem.comments.size() >= 2; i++){
            Comment comment = fItem.comments.get(i);
            View v = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, null);

            ImageView img = (ImageView) v.findViewById(R.id.userImage);
            Picasso.with(getContext())
                    .load(comment.profilePictureUrl)
                    .resize(150, 150)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .transform(transformation)
                    .into(img);

            TextView txtUsername = (TextView) v.findViewById(R.id.userName);
            txtUsername.setText(comment.username);

            TextView txtComment = (TextView) v.findViewById(R.id.userComment);
            txtComment.setText(comment.commentText);
            feedItemViewHolder.commentList.addView(v);
        }

        return convertView;
    }
}
