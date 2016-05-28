package com.yiyan.facebookphotoviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    JSONArray photoJsonArray = null;
    ListView photoListView;

    PhotoListAdapter photoListAdapter;
    ArrayList<Photo> photoObjList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();

        TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);

        photoListView = (ListView)findViewById(R.id.photo_list);
        photoObjList = new ArrayList<>();
        photoListAdapter = new PhotoListAdapter(this, photoObjList);
        photoListView.setAdapter(photoListAdapter);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/albums",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject = response.getJSONObject();
                        try {
                            JSONArray albumJsonArray = jsonObject.getJSONArray("data");
                            for(int i = 0; i < albumJsonArray.length(); i++){
                                JSONObject albumJsonObject = albumJsonArray.getJSONObject(i);
                                getPhotosFromAlbum(albumJsonObject.getString("id"), albumJsonObject.getString("name"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public void getPhotosFromAlbum(String albumId, final String albumName) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject = response.getJSONObject();
                        try {
                            photoJsonArray = jsonObject.getJSONArray("data");
                            for(int i = 0; i < photoJsonArray.length(); i++){
                                JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
                                photoObjList.add(new Photo(photoJsonObject, albumName));
                            }
                            photoListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(MainActivity.this, FacebookLoginActivity.class);
        startActivity(login);
        finish();
    }
}
