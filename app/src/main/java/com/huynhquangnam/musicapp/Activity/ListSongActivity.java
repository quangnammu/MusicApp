package com.huynhquangnam.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;

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
        handleIntent(getIntent());

    }
    private void handleIntent(Intent intent){
        if(intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String query = intent.getStringExtra(MainActivity.SONG_NAME);
            for (int i = 0; i < ListSong.getListSong().size(); i++) {
                if (ListSong.getListSong().get(i).getSongName().toLowerCase().contains(query.toLowerCase())) {
                    songListAdapter.addItem(ListSong.getListSong().get(i));
                }
            }
            songListAdapter.notifyDataSetChanged();
        }else if(intent.getAction().equals(SongsElement.OFF_SONG)){
            Cursor cursor = StorageUtil.getMP3FileCursor(this);
            if (cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        songListAdapter.addItem(new Song(
                                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                                "",
                                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)),
                                ""));
                    }while (cursor.moveToNext());
                }
            }
        }
    }
}
