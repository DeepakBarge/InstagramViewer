package com.example.deepak.instagramviewer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class FeedItemAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private Context context;
    private ArrayList<FeedItem> feedItems = new ArrayList<>();
    private LayoutInflater inflater;

    public void addAll(ArrayList<FeedItem> items){
        feedItems.addAll(items);
    }
    @Override
    public long getHeaderId(int position) {
        return new Random().nextLong();
    }

    class FeedItemHeaderViewHolder {

        @Bind(R.id.username) TextView username;
        @Bind(R.id.profileImage) ImageView profileImage;
        @Bind(R.id.timestamp) TextView relativeTimestamp;

        public FeedItemHeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    class FeedItemBodyViewHolder {

        @Bind(R.id.caption) TextView caption;
        @Bind(R.id.likeCount) TextView likeCount;
        @Bind(R.id.feedImage) ImageView feedImage;
        @Bind(R.id.playImage) ImageView playImage;
        @Bind(R.id.viewAllComments) TextView viewAllComments;
        @Bind(R.id.commentsLayout) LinearLayout commentList;

        public FeedItemBodyViewHolder(View view) {
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
        //super(context, 0, feedItems);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int position) {
        return feedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        final FeedItem fItem = (FeedItem) getItem(position);

        FeedItemHeaderViewHolder feedItemHeaderViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_header, parent, false);
            feedItemHeaderViewHolder = new FeedItemHeaderViewHolder(convertView);
            convertView.setTag(feedItemHeaderViewHolder);
        } else {
            feedItemHeaderViewHolder = (FeedItemHeaderViewHolder) convertView.getTag();
        }
        feedItemHeaderViewHolder.profileImage.setImageResource(0);

        Picasso.with(context)
                .load(fItem.profilePictureUrl)
                .transform(transformation)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(feedItemHeaderViewHolder.profileImage);

        feedItemHeaderViewHolder.relativeTimestamp.setText(fItem.relativeTimestampString);

        feedItemHeaderViewHolder.username.setText(fItem.username);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final FeedItem fItem = (FeedItem) getItem(position);

        FeedItemBodyViewHolder feedItemBodyViewHolder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_body, parent, false);
            feedItemBodyViewHolder = new FeedItemBodyViewHolder(convertView);
            convertView.setTag(feedItemBodyViewHolder);

        } else {
            feedItemBodyViewHolder = (FeedItemBodyViewHolder) convertView.getTag();
        }

        feedItemBodyViewHolder.viewAllComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewAllCommentsActivity.class);
                i.putExtra("item", fItem);
                context.startActivity(i);
            }
        });


        feedItemBodyViewHolder.caption.setText(fItem.caption);

        feedItemBodyViewHolder.likeCount.setText(String.valueOf(fItem.likeCount));

        feedItemBodyViewHolder.feedImage.setImageResource(0);

        Picasso.with(context)
                .load(fItem.imageUrl)
                .into(feedItemBodyViewHolder.feedImage);


        if(fItem.mediaType == FeedItem.IMAGE){
            feedItemBodyViewHolder.playImage.setVisibility(View.INVISIBLE);
        } else {
            feedItemBodyViewHolder.playImage.setVisibility(View.VISIBLE);
        }

        //LinearLayout commentList = (LinearLayout) convertView.findViewById(R.id.commentsLayout);
        feedItemBodyViewHolder.commentList.removeAllViews();

        //display only top 2 comments
        for(int i=0; i<2 && fItem.comments.size() >= 2; i++){
            Comment comment = fItem.comments.get(i);
            View v = LayoutInflater.from(context).inflate(R.layout.item_comment, null);

            ImageView img = (ImageView) v.findViewById(R.id.userImage);
            Picasso.with(context)
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
            feedItemBodyViewHolder.commentList.addView(v);
        }

        return convertView;
    }
}
