package com.example.android.homework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private int charCount;
    private int epiCount;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private ImageView imageView_title;

    private String base_api_url = "https://rickandmortyapi.com/api/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get number of characters
        getCharacterCount();
        // get number of episodes
        getEpisodeCount();

        imageView_title = findViewById(R.id.imageView_title);
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("mortyandrick.png");
            Drawable brew = Drawable.createFromStream(inputStream, null);
            imageView_title.setImageDrawable(brew);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load nothing at first
        // load fragment on click
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){


                    // get random int between 0 and character count + 1
                    Random r = new Random();
                    int rand = r.nextInt(charCount + 1) + 1;
                    Log.println(Log.INFO, "count", String.valueOf(charCount));
                    Log.println(Log.INFO, "api_url", base_api_url + "character/" + rand);

                    // call api to get random character
                    client.get(base_api_url + "character/" + rand, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                JSONObject json = new JSONObject(new String(responseBody));

                                // get name, status, gender, origin name, location name, image, list of episode appearances
                                String name = json.getString("name");
                                String species = json.getString("species");
                                String status = json.getString("status");
                                String gender = json.getString("gender");
                                String originName = json.getJSONObject("origin").getString("name");
                                String locationName = json.getJSONObject("location").getString("name");
                                String image_url = json.getString("image");
                                String episodeAppearances = "";
                                for (int i = 0; i < json.getJSONArray("episode").length(); i++){
                                    if( i != json.getJSONArray("episode").length() - 1) {
                                        episodeAppearances += String.valueOf(json.getJSONArray("episode").get(i)).replace("https://rickandmortyapi.com/api/episode/", "") + ", ";
                                    }
                                    else{
                                        episodeAppearances += String.valueOf(json.getJSONArray("episode").get(i)).replace("https://rickandmortyapi.com/api/episode/", "");
                                    }
                                }


                                // package info for fragment
                                Bundle bundle = new Bundle();
                                bundle.putString("name", name);
                                bundle.putString("species", species);
                                bundle.putString("status", status);
                                bundle.putString("gender", gender);
                                bundle.putString("originName", originName);
                                bundle.putString("locationName", locationName);
                                bundle.putString("image_url", image_url);
                                bundle.putString("episodes", episodeAppearances);

                                // load fragment and send info
                                CharacterFragment characterFragment = new CharacterFragment();
                                characterFragment.setArguments(bundle);
                                loadFragment(characterFragment, R.id.fragContainer);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.e("api error", new String(responseBody));
                        }
                    });
                }

                else if(position == 1){

                    // get random int between 0 and character count + 1
                    Random r1 = new Random();
                    int rand1 = r1.nextInt(epiCount + 1) + 1;
                    Log.println(Log.INFO, "count", String.valueOf(epiCount));
                    Log.println(Log.INFO, "api_url", base_api_url + "character/" + rand1);

                    // call api to get random episode
                    client.get(base_api_url + "episode/" + rand1, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            // episode number, name, air date, "more information" button, first 3 characters' names OR/AND their images in this episode
                            try {
                                JSONObject json = new JSONObject(new String(responseBody));

                                String episode = json.getString("episode");
                                String name = json.getString("name");
                                String air_date = json.getString("air_date");
                                String more_info = "https://rickandmorty.fandom.com/wiki/" + name;

                                // convert JSONArray to String[]
                                JSONArray characters = json.getJSONArray("characters");
                                String[] imageList = new String[json.getJSONArray("characters").length()];
                                for(int i = 0; i < imageList.length; i++){

                                    imageList[i] = String.valueOf(characters.get(i));
                                }
                                Bundle bundle = new Bundle();
                                bundle.putString("episode", episode);
                                bundle.putString("name", name);
                                bundle.putString("air_date", air_date);
                                bundle.putStringArray("imageList", imageList);
                                bundle.putString("more_info", more_info);


                                // load fragment and send info
                                EpisodeFragment episodeFragment = new EpisodeFragment();
                                episodeFragment.setArguments(bundle);
                                loadFragment(episodeFragment, R.id.fragContainer);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.e("api error", new String(responseBody));
                        }
                    });
                }
                else if(position == 2){
                    LocationFragment locationFragment = new LocationFragment();
                    loadFragment(locationFragment, R.id.fragContainer);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    public void getCharacterCount(){
        client.get(base_api_url + "character/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    charCount = json.getJSONObject("info").getInt("count");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("api error", new String(responseBody));
            }
        });
    }

    public void getEpisodeCount(){
        client.get(base_api_url + "episode/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    epiCount = json.getJSONObject("info").getInt("count");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("api error", new String(responseBody));
            }
        });
    }


    public void loadFragment(Fragment fragment, int id){
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}