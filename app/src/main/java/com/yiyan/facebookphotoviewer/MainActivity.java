package com.yiyan.facebookphotoviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    JSONArray albumJsonArray = null;
    ListView photoListView;
    ArrayAdapter mAdapter;
    ArrayList<String> photoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();

        TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);

        photoListView = (ListView)findViewById(R.id.photo_list);
        mAdapter = new ArrayAdapter<>(this,
                R.layout.item_list_photo,
                R.id.photo_title,
                photoList);
        photoListView.setAdapter(mAdapter);

        // get albums
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/albums",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject = response.getJSONObject();
                        try {
                            albumJsonArray = jsonObject.getJSONArray("data");
                            for(int i = 0; i < albumJsonArray.length(); i++){
                                JSONObject oneAlbum = albumJsonArray.getJSONObject(i);
                                photoList.add(oneAlbum.getString("name"));
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

        for(int i = 0; i < albumJsonArray.length(); i++){
            JSONObject oneAlbum = null;
            try {
                oneAlbum = albumJsonArray.getJSONObject(i);
                Log.d("album nameeee", oneAlbum.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        new DownloadImage((ImageView)findViewById(R.id.profileImage)).execute("https://graph.facebook.com/865787606822791/picture?type=normal");
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
