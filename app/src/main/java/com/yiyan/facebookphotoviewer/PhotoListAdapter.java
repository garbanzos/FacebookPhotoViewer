package com.yiyan.facebookphotoviewer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;

import java.util.List;

public class PhotoListAdapter extends ArrayAdapter<Photo> {

    private List<Photo> photoList;
    private LayoutInflater inflater;

    private class ViewHolder {
        ImageView photoImageView;
        TextView photoTitleView;
        TextView photoAlbumView;
    }

    public PhotoListAdapter(Context context, List<Photo> photoList) {
        super(context, R.layout.item_list_photo, photoList);
        inflater = LayoutInflater.from(context);
        this.photoList = photoList;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_photo, null);
            holder.photoTitleView = (TextView) convertView.findViewById(R.id.photo_title);
            holder.photoAlbumView = (TextView) convertView.findViewById(R.id.photo_album);
            holder.photoImageView = (ImageView) convertView.findViewById(R.id.photo_image_view);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Photo photoItem = photoList.get(position);

        holder.photoTitleView.setText(photoItem.getPhotoTitle());
        holder.photoAlbumView.setText(photoItem.getPhotoAlbum());

        new DownloadImage(holder.photoImageView).execute("https://graph.facebook.com/" + photoItem.getPhotoId() + "/picture?type=normal&access_token=" + AccessToken.getCurrentAccessToken().getToken());
        return convertView;
    }
}
