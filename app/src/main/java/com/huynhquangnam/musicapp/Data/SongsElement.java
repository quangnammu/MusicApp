package com.huynhquangnam.musicapp.Data;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;

import com.huynhquangnam.musicapp.Activity.ListSongActivity;
import com.huynhquangnam.musicapp.Element.Song;
import com.huynhquangnam.musicapp.R;
import com.huynhquangnam.musicapp.Util.StorageUtil;

public class SongsElement extends BaseListElement {

    public static String OFF_SONG = "off_song";

    public SongsElement(Context context) {
        this.context = context;
        updateData();
    }

    @Override
    public void updateData() {
        this.setElementName("Song");
        this.setIconResource(R.drawable.ic_note_song);
        this.setNumber(getSizeList());
    }

    private int getSizeList() {
        int size = 0;
        Cursor cursor = StorageUtil.getMP3FileCursor(context);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    if (!cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)).equals("<unknown>")) {
                        size++;
                    }
                } while (cursor.moveToNext());
            }
        }
        return size;
    }

    @Override
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongsElement.this.context, ListSongActivity.class);
                intent.setAction(OFF_SONG);
                SongsElement.this.context.startActivity(intent);
            }
        };
    }
}
