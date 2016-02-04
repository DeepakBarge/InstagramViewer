package com.example.deepak.instagramviewer;

import java.util.ArrayList;

/**
 * Created by deepak on 2/3/16.
 */
public class FeedItem {

    public String username;
    public String profilePictureUrl;
    public String caption;
    public String mediaType;
    public String imageUrl;
    public String videoUrl;
    public int likeCount;
    public int commentCount;
    public String createdTime;
    public ArrayList<Comment> comments;

    /*public FeedItem(String username, String profilePictureUrl, String caption, String mediaType, String imageUrl,
                    int likeCount, int commentCount, String createdTime, ArrayList<Comment> comments) {
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.caption = caption;
        this.mediaType = mediaType;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdTime = createdTime;
        this.comments = comments;
    }*/

    public FeedItem(){}
}

class Comment {

    public String username;
    public String commentText;
    public String profilePictureUrl;
}