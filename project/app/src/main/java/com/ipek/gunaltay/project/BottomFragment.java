package com.ipek.gunaltay.project;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BottomFragment extends Fragment {

    ImageView play;
    TextView songName;
    public boolean isPlaying;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        play = view.findViewById(R.id.imagePlayBF);
        songName = view.findViewById(R.id.textSongNameBF);

        setIcon(isPlaying);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = !isPlaying;
                setIcon(isPlaying);

                if(isPlaying) {
                    Intent intent = new Intent(getActivity(), MusicPlayerService.class);
                    getActivity().startService(intent);
                } else {
                    Intent intent = new Intent(getActivity(), MusicPlayerService.class);
                    getActivity().stopService(intent);
                }
            }
        });
    }

    public void setIcon(boolean playing) {
        if(playing == true) {
            play.setImageResource(R.drawable.ic_baseline_pause_24);
            Log.d("ISPLAYING", "" + getResources().getResourceName(play.getId()));
        } else if(playing == false) {
            play.setImageResource(R.drawable.ic_baseline_play_arrow_28);
            Log.d("ISPLAYING", "" + getResources().getResourceName(play.getId()));
        }
    }

    public void setPlaying(boolean playing) {
        Log.d("ISPLAYING", "Setting playing to: " + playing);
        isPlaying = playing;
        setIcon(playing);
    }
}