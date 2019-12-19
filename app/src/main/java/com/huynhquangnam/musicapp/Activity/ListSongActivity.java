package com.huynhquangnam.musicapp.Activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.huynhquangnam.musicapp.Adapter.SongListAdapter;
import com.huynhquangnam.musicapp.Data.ListSong;
import com.huynhquangnam.musicapp.Data.SongsElement;
import com.huynhquangnam.musicapp.Element.Song;
import com.huynhquangnam.musicapp.R;
import com.huynhquangnam.musicapp.Util.StorageUtil;

public class ListSongActivity extends AppCompatActivity {

    ListView listSong;
    SongListAdapter songListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);

        listSong = findViewById(R.id.list_song);

        songListAdapter = new SongListAdapter(this);
        listSong.setAdapter(songListAdapter);
//        listSong.setOnItemClickListener(songListAdapter);
        handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String query = intent.getStringExtra(MainActivity.SONG_NAME);
            for (int i = 0; i < ListSong.getListSong().size(); i++) {
                if (ListSong.getListSong().get(i).getSongName().toLowerCase().contains(query.toLowerCase())) {
                    songListAdapter.addItem(ListSong.getListSong().get(i));
                }
            }
            songListAdapter.notifyDataSetChanged();
        } else if (intent.getAction().equals(SongsElement.OFF_SONG)) {
            Cursor cursor = StorageUtil.getMP3FileCursor(this);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        if (!cursor.getString(2).equals("<unknown>")) {
                            songListAdapter.addItem(new Song(
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getString(3),
                                    ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(0)).toString(),
                                    ""));
                        }
                    } while (cursor.moveToNext());
                }
            }
        }

    }
}
