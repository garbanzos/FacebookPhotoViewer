package com.yiyan.facebookphotoviewer;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo implements Comparable<Photo> {

    private String photoTitle;
    private String photoId;
    private String photoAlbum;

    public Photo(JSONObject photoJsonObject, String photoAlbum) {
        try {
            this.photoTitle = photoJsonObject.has("name") ? photoJsonObject.getString("name") : "no title";
            this.photoId = photoJsonObject.getString("id");
            this.photoAlbum = photoAlbum;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getPhotoAlbum() {
        return photoAlbum;
    }

    @Override
    public int compareTo(Photo another) {
        return this.photoAlbum.compareTo(another.photoAlbum);
    }
}
