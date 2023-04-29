package com.example.project;

public class Video {
    private String mTitle;
    private String mThumbnailUrl;
    private String mVideoId;
    private String mVideoUrl;

    public Video(String title, String thumbnailUrl, String videoId, String videoUrl) {
        mTitle = title;
        mThumbnailUrl = thumbnailUrl;
        mVideoId = videoId;
        mVideoUrl = videoUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }
}

