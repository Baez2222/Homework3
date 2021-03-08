package com.example.android.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.entity.mime.Header;

public class LocationFragment extends Fragment {

    private RecyclerView recyclerView;
    private static AsyncHttpClient client = new AsyncHttpClient();
    String api_url = "https://rickandmortyapi.com/api/location/";
    private View view;
    private ArrayList<Location> locationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);

        locationList = new ArrayList<>();

        client.get(api_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    JSONArray jsonArray = json.getJSONArray("results");

                    for (int i = 0; i < 20; i++) {
                        Location location = new Location(jsonArray.getJSONObject(i).getString("name"),
                                jsonArray.getJSONObject(i).getString("type"),
                                jsonArray.getJSONObject(i).getString("dimension"));
                        locationList.add(location);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView = view.findViewById(R.id.recyclerView_location);
                LocationAdapter adapter = new LocationAdapter(locationList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("api error", new String(responseBody));
            }
            });

        return view;
    }
}
