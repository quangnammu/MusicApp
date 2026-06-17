package com.huynhquangnam.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huynhquangnam.musicapp.R;
import com.huynhquangnam.musicapp.service.PlayerService;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {

    TextView timeStartTv;
    TextView timeEndTv;
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

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusMedia();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendCurrentPositionToService(seekBar.getProgress());
            }
        });

        IntentFilter filter = new IntentFilter("sendDuration");
        filter.addAction("sendProgress");

        LocalBroadcastManager.getInstance(this).registerReceiver(createMessageReceiver(), filter);

        Intent i = getIntent();
        startService(createIntentService(i.getStringExtra("songName"), i.getStringExtra("songURL")));
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
                        String endTime = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MICROSECONDS.toMinutes(duration),
                                TimeUnit.MICROSECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(duration));
                        timeEndTv.setText(endTime);
                        break;
                    case "sendProgress":
                        int progress = intent.getIntExtra("progress", 0);
                        int currentPosition = intent.getIntExtra("currentPosition", 0);

                        seekBar.setProgress(progress);

                        String startTime = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MICROSECONDS.toMinutes(currentPosition),
                                TimeUnit.MICROSECONDS.toSeconds(currentPosition) - TimeUnit.MINUTES.toSeconds(currentPosition));
                        timeStartTv.setText(startTime);
                        break;
                }
            }
        };
    }

    private Intent createIntentService(String name, String url) {
        Intent i = new Intent(this, PlayerService.class);
        i.putExtra("songName", name);
        i.putExtra("songURL", url);
        return i;
    }
}
