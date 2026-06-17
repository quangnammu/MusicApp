package com.huynhquangnam.musicapp.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.huynhquangnam.musicapp.R;
import com.huynhquangnam.musicapp.service.PlayerService;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {

    TextView timeStartTv;
    TextView timeEndTv;
    TextView songNameTv;
    TextView songSingerTv;
    SeekBar seekBar;
    Button statusButton;
    boolean isPlay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        timeStartTv = findViewById(R.id.time_start);
        timeEndTv = findViewById(R.id.time_end);
        seekBar = findViewById(R.id.progress_bar);
        statusButton = findViewById(R.id.status_button);
        songNameTv = findViewById(R.id.song_name);
        songSingerTv = findViewById(R.id.song_singer);

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusMedia();
            }
        });

        Intent intent = getIntent();
        String songName = intent.getStringExtra("songName");
        String songSinger = intent.getStringExtra("songSinger");
        String songURL = intent.getStringExtra("songURL");

        songNameTv.setText(songName);
        songSingerTv.setText(songSinger);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendCurrentPositionToService(seekBar.getProgress());
            }
        });

        IntentFilter filter = new IntentFilter("sendDuration");
        filter.addAction("sendProgress");

        LocalBroadcastManager.getInstance(this).registerReceiver(createMessageReceiver(), filter);

        Intent i = getIntent();
        Log.d("xxx", i.getStringExtra("songName") + " -- " + i.getStringExtra("songURL"));
        startService(createIntentService(songName, songURL, songSinger));
    }

    private void sendCurrentPositionToService(int currentPosition) {
        Intent intent = new Intent("seekChange");
        intent.putExtra("currentPosition", currentPosition);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void changeStatusMedia() {
        Intent intent = new Intent("changeStatusMedia");
        isPlay = !isPlay;
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        statusButton.setBackgroundResource(isPlay ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private BroadcastReceiver createMessageReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "sendDuration":
                        int duration = intent.getIntExtra("duration", 0);
                        long second = (duration / 1000) % 60;
                        long minute = (duration / (1000 * 60)) % 60;
                        String endTime = String.format(Locale.ENGLISH, "%02d:%02d", minute, second);
                        timeEndTv.setText(endTime);
                        break;
                    case "sendProgress":
                        int progress = intent.getIntExtra("progress", 0);
                        int currentPosition = intent.getIntExtra("currentPosition", 0);

                        seekBar.setProgress(progress);
                        long second2 = (currentPosition / 1000) % 60;
                        long minute2 = (currentPosition / (1000 * 60)) % 60;
                        String startTime = String.format(Locale.ENGLISH, "%02d:%02d", minute2, second2);
                        timeStartTv.setText(startTime);
                        break;
                }
            }
        };
    }

    private Intent createIntentService(String name, String url, String singer) {
        Intent i = new Intent(this, PlayerService.class);
        i.putExtra("songName", name);
        i.putExtra("songURL", url);
        i.putExtra("songSinger", singer);
        return i;
    }
}
