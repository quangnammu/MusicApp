package com.huynhquangnam.musicapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huynhquangnam.musicapp.Element.Song;
import com.huynhquangnam.musicapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class SongListAdapter extends BaseAdapter {

    public ArrayList<Song> listSong;
    public Context context;

    public SongListAdapter (Context context){
        this.context = context;
        this. listSong = new ArrayList<>();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
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

        TextView songName = view.findViewById(R.id.song_name);
        TextView songSinger = view.findViewById(R.id.song_singer);
        ImageView songImage = view.findViewById(R.id.song_image);

        ImageLoader.getInstance().displayImage(listSong.get(i).getSongImageURL(), songImage);
        songName.setText(listSong.get(i).getSongName());
        songSinger.setText(listSong.get(i).getSongSinger());

        return view;
    }
    public void addItem(Song song){
        if( listSong != null && song != null){
            listSong.add(song);
        }
    }
}
