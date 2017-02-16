package com.sk.greate43.flickrbrowser;

import java.io.Serializable;

/**
 * Created by great on 2/10/2017.
 */

class Photo implements Serializable {

    private static long serialVersionUID = 1L;

    private String mTitle;
    private String mAuthor;
    private String mAuthorId;
    private String mLink;
    private String mTags;
    private String mImage;

    public Photo(String title, String author, String authorId, String link, String tags, String image) {

        mTitle = title;
        mAuthor = author;
        mAuthorId = authorId;
        mLink = link;
        mTags = tags;
        mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public String getLink() {
        return mLink;
    }

    public String getTags() {
        return mTags;
    }

    public String getImage() {
        return mImage;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\n' +
                ", mAuthor='" + mAuthor + '\n' +
                ", mAuthorId='" + mAuthorId + '\n' +
                ", mLink='" + mLink + '\n' +
                ", mTags='" + mTags + '\n' +
                ", mImage='" + mImage + '\n' +
                '}';
    }
}
