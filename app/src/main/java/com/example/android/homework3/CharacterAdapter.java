package com.example.android.homework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    private List<Character> characters;

    public CharacterAdapter(List<Character> characters){
        this.characters = characters;
    }

    @NonNull
    @Override
    public CharacterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // used to inflate a layout from xml and return the ViewHolder
        // very standard code/template looking code
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // inflate the custom layout
        View characterView = inflater.inflate(R.layout.item_character, parent, false);
        // return a new ViewHolder
        CharacterAdapter.ViewHolder viewHolder = new CharacterAdapter.ViewHolder(characterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterAdapter.ViewHolder holder, int position) {

        Character character = characters.get(position);

        Picasso.get().load(character.getImage_url()).into(holder.imageView_recycler);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView_recycler;

        public ViewHolder (View itemView){
            super(itemView);

            imageView_recycler = itemView.findViewById(R.id.imageView_recycler);
        }
    }
}
