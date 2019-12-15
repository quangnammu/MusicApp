package com.huynhquangnam.musicapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.huynhquangnam.musicapp.Element.Song;
import com.huynhquangnam.musicapp.R;

import java.util.ArrayList;

public class SongListAdapter extends BaseAdapter {

    public ArrayList<Song> listSong;
    public Context context;

    public SongListAdapter (Context context){
        this.context = context;
        this. listSong = new ArrayList<>();

    }
    @Override
    public int getCount() {
        return listSong.size();
    }

    @Override
    public Object getItem(int i) {
        return listSong.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = View.inflate(context, R.layout.song_list_item, null);
        return view;
    }
}
