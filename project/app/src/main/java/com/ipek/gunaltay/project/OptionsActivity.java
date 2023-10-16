package com.ipek.gunaltay.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.tabs.TabLayout;

public class OptionsActivity extends AppCompatActivity {

    Intent receivedIntent;
    BottomFragment bottomFragment;
    SongListFragment songListFragment;
    TabLayout tabLayout;
    Button buttonBack;
    SpinKitView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action and status bars
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_options);

        Intent serviceIntent = new Intent(OptionsActivity.this, JsonIntentService.class);
        startService(serviceIntent);

        bottomFragment = (BottomFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBottomO);
        tabLayout = findViewById(R.id.tabLayout);
        buttonBack = findViewById(R.id.buttonBackO);
        songListFragment = (SongListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentSongList);
        loader = findViewById(R.id.spinKitLoader);

        loader.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentSongList, songListFragment);
        fragmentTransaction.commit();

        IntentFilter filter = new IntentFilter();
        filter.addAction("JSON_PARSE_COMPLETED_ACTION");
        registerReceiver(mbroadcastreciver, filter);

        receivedIntent = getIntent();

        if(receivedIntent != null) {
            Log.d("ISPLAYING", "RECEIVED INTENT: " + receivedIntent.getBooleanExtra("isPlaying", false));
            boolean p = receivedIntent.getBooleanExtra("isPlaying", false);
            Log.d("ISPLAYING", "P:" + p);
            //bottomFragment.setPlaying(p);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        songListFragment.updateList(Commons.mc_songs, "minecraft");
                        break;
                    case 1:
                        songListFragment.updateList(Commons.sh_songs, "silentHill");
                        break;
                    case 2:
                        songListFragment.updateList(Commons.ts2_songs, "theSims2");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private BroadcastReceiver mbroadcastreciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tabLayout.setVisibility(View.VISIBLE);
            loader.setVisibility(View.INVISIBLE);
            songListFragment.updateList(Commons.mc_songs, "minecraft");
        }
    };
}