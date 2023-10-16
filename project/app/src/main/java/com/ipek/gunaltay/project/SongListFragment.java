package com.ipek.gunaltay.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongListFragment extends Fragment {
    TextView textSongList;
    ImageView imageTheme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_song_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textSongList = view.findViewById(R.id.textSongNamesHolder);
        imageTheme = view.findViewById(R.id.imageSongTheme);
    }

    public void updateList(ArrayList<String> songList, String theme) {
        switch (theme) {
            case "minecraft":
                imageTheme.setImageResource(R.drawable.minecraft);
                break;
            case "silentHill":
                imageTheme.setImageResource(R.drawable.silent_hill);
                break;
            case "theSims2":
                imageTheme.setImageResource(R.drawable.sims2);
                break;
        }
        String content = "";
        for(String s : songList) {
            content += "• " + s + "\n";
        }
        textSongList.setText(content);
    }
}