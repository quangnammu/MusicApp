package com.huynhquangnam.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.huynhquangnam.musicapp.R;
import com.huynhquangnam.musicapp.service.PlayerService;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent i = getIntent();
        startService(createIntentService(i.getStringExtra("songNamre"), i.getStringExtra("songURL")));
    }

    private Intent createIntentService(String name, String url) {
        Intent i = new Intent(this, PlayerService.class);
        i.putExtra("songName", name);
        i.putExtra("songURL", url);
        return  i;
    }
}
