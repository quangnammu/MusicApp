package com.huynhquangnam.musicapp.Data;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.huynhquangnam.musicapp.Activity.ListSongActivity;
import com.huynhquangnam.musicapp.R;
import com.huynhquangnam.musicapp.Util.StorageUtil;

public class SongsElement extends BaseListElement {

    public static String OFF_SONG ="off_song";
    public SongsElement(Context context){
        this.context = context;
        updateData();
    }

    @Override
    public void updateData() {
        this.setElementName("Song");
        this.setIconResource(R.drawable.ic_note_song);
        this.setNumber(StorageUtil.getMP3FileCursor(this.context).getCount());
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
