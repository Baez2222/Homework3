package com.example.android.homework3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class CharacterFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_character, container, false);




        // update textViews
        TextView textView_charName = (TextView) view.findViewById(R.id.textView_charName);
        textView_charName.setText(getArguments().getString("name").toUpperCase());
        TextView textView_species = (TextView) view.findViewById(R.id.textView_species);
        textView_species.setText("Species: " + getArguments().getString("species"));
        TextView textView_charStatus = (TextView) view.findViewById(R.id.textView_charStatus);
        textView_charStatus.setText("Status: " + getArguments().getString("status"));
        TextView textView_charGender = (TextView) view.findViewById(R.id.textView_charGender);
        textView_charGender.setText("Gender: " + getArguments().getString("gender"));
        TextView textView_charOriginName = (TextView) view.findViewById(R.id.textView_charOriginName);
        textView_charOriginName.setText("Origin: " + getArguments().getString("originName"));
        TextView textView_charLocationName = (TextView) view.findViewById(R.id.textView_charLocationName);
        textView_charLocationName.setText("Location: " + getArguments().getString("locationName"));
        TextView textView_charEpisodes = (TextView) view.findViewById(R.id.textView_charEpisodes);
        textView_charEpisodes.setText("Episodes Appeared in: " + getArguments().getString("episodes"));
        ImageView imageView_charImage = (ImageView) view.findViewById(R.id.imageView_charImage);
        Picasso.get().load(getArguments().getString("image_url")).into(imageView_charImage);

        return view;
    }
}
