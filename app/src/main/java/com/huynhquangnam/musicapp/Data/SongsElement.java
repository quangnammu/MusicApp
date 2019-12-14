package com.huynhquangnam.musicapp.Data;

import android.content.Context;
import android.view.View;

import com.huynhquangnam.musicapp.R;

public class SongsElement extends BaseListElement {
    public SongsElement(Context context){
        this.context = context;
        updateData();
    }

    @Override
    public void updateData() {
        this.setElementName("Song");
        this.setIconResource(R.drawable.ic_note_song);
        this.setNumber(0);
    }

    @Override
    public View.OnClickListener getOnClickListener() {
        return null;
    }
}
