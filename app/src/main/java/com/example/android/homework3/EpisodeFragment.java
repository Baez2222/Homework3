package com.example.android.homework3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class EpisodeFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private ArrayList<Character> characters;
    private Button more_info;
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_episode, container, false);

        // update textViews
        TextView textView_name = (TextView) view.findViewById(R.id.textView_title);
        textView_name.setText(getArguments().getString("episode") + " " + getArguments().getString("name"));

        TextView textView_air_date = (TextView) view.findViewById(R.id.textView_air_date);
        textView_air_date.setText("Aired on: " + getArguments().getString("air_date"));

        more_info = view.findViewById(R.id.button_more_info);

        recyclerView = view.findViewById(R.id.recyclerView_episodeFrag);

        characters = new ArrayList<>();
        String[] imageList = getArguments().getStringArray("imageList");

//        Log.println(Log.INFO, "recycler_name", getArguments().getStringArray("imageList")[0]);

        for (int i = 0; i < imageList.length; i++){
            String char_url = imageList[i];
//            Log.println(Log.INFO, "recycler_name", charList[i]);

            client.get(char_url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        String image_url = json.getString("image");
                        Log.println(Log.INFO, "recycler_image", image_url);

                        Character character = new Character(image_url);

                        characters.add(character);

                        CharacterAdapter adapter = new CharacterAdapter(characters);
                        recyclerView.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
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


//        CharacterAdapter adapter = new CharacterAdapter(characters);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        String url = "https://rickandmorty.fandom.com/wiki/" + getArguments().getString("name");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "CHANNEL_ID")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getArguments().getString("name").toUpperCase())
                .setContentText("Click here to learn more:")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
        more_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notificationManager.notify(100, builder.build());
            }
        });

        return view;
    }
}
