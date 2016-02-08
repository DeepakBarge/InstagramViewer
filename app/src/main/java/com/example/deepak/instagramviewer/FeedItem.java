package com.example.deepak.instagramviewer;

import java.io.Serializable;
import java.util.ArrayList;

public class FeedItem implements Serializable {

    public static final int IMAGE = 0;
    public static final int VIDEO = 1;

    public String username;
    public String profilePictureUrl;
    public String caption;
    public int mediaType;
    public String imageUrl;
    public String videoUrl;
    public int likeCount;
    public int commentCount;
    public String createdTime;
    public String relativeTimestampString;
    public ArrayList<Comment> comments = new ArrayList<>();

    public FeedItem(){}
}

class Comment implements Serializable{

    public String username;
    public String commentText;
    public String profilePictureUrl;
}