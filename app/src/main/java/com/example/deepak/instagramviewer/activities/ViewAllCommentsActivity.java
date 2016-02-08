package com.example.deepak.instagramviewer.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.deepak.instagramviewer.models.Comment;
import com.example.deepak.instagramviewer.models.FeedItem;
import com.example.deepak.instagramviewer.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ViewAllCommentsActivity extends AppCompatActivity {

    static Transformation transformation = new RoundedTransformationBuilder()
            .borderColor(Color.GRAY)
            .borderWidthDp(1)
            .cornerRadiusDp(30)
            .oval(false)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_comments);

        Intent intent = getIntent();
        FeedItem item = (FeedItem) intent.getSerializableExtra("item");

        ImageView imgComment = (ImageView) findViewById(R.id.commentImage);

        Picasso.with(getApplicationContext())
                .load(item.imageUrl)
                .into(imgComment);

        TextView txtCaption = (TextView) findViewById(R.id.caption);
        txtCaption.setText(item.caption);

        LinearLayout commentList = (LinearLayout) findViewById(R.id.commentsLayout);
        //commentList.removeAllViews();

        for(int i=0; i<item.comments.size(); i++) {
            Comment comment = item.comments.get(i);
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_comment, null);

            ImageView imgUser = (ImageView) v.findViewById(R.id.userImage);
            Picasso.with(getApplicationContext())
                    .load(comment.profilePictureUrl)
                    .resize(150, 150)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .transform(transformation)
                    .into(imgUser);

            TextView txtUsername = (TextView) v.findViewById(R.id.userName);
            txtUsername.setText(comment.username);

            TextView txtComment = (TextView) v.findViewById(R.id.userComment);
            txtComment.setText(comment.commentText);

            commentList.addView(v);
        }
    }
}