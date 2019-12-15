package com.huynhquangnam.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.huynhquangnam.musicapp.R;

public class ListSongActivity extends AppCompatActivity {

    ListView listSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        listSong = findViewById(R.id.list_song);

    }
}
