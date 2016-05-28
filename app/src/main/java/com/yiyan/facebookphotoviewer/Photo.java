package com.yiyan.facebookphotoviewer;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {

    private String photoTitle;
    private String photoId;
    private String photoAlbum;

    public Photo(JSONObject photoJsonObject) {
        try {
            photoTitle = photoJsonObject.has("name") ? photoJsonObject.getString("name") : "no title";
            photoId = photoJsonObject.getString("id");

            //TODO get album
            photoAlbum = "photo album title";

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
}
