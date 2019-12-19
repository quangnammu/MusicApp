package com.huynhquangnam.musicapp.Adapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huynhquangnam.musicapp.Activity.PlayerActivity;
import com.huynhquangnam.musicapp.Element.Song;
import com.huynhquangnam.musicapp.R;
import com.huynhquangnam.musicapp.Util.StorageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class SongListAdapter extends BaseAdapter {

    public ArrayList<Song> listSong;
    public Context context;
    DownloadManager downloadManager;
    long enqueue;

    public SongListAdapter(Context context) {
        this.context = context;
        this.listSong = new ArrayList<>();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = View.inflate(context, R.layout.song_list_item, null);

        TextView songName = view.findViewById(R.id.song_name);
        TextView songSinger = view.findViewById(R.id.song_singer);
        ImageView songImage = view.findViewById(R.id.song_image);
        Log.d("xxx", listSong.get(i).getSongImageURL());

        if (listSong.get(i).getSongImageURL().endsWith(".jpg")) {
            ImageLoader.getInstance().displayImage(listSong.get(i).getSongImageURL(), songImage);
        } else {
            Bitmap imgBitmap = StorageUtil.getCoverPicture(context, Uri.parse(listSong.get(i).getSongImageURL()));
            if (imgBitmap != null) {
                songImage.setImageBitmap(imgBitmap);
            } else {
                songImage.setImageResource(R.drawable.ic_note_song);
            }
        }

        songName.setText(listSong.get(i).getSongName());
        songSinger.setText(listSong.get(i).getSongSinger());


        Button downloadButton = view.findViewById(R.id.download_button);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Toast.makeText(context, "Download thanh cong", Toast.LENGTH_SHORT).show();
                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(listSong.get(i).getSongURL()));

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                        .setTitle(listSong.get(i).getSongName() + ".mp3")
                        .setDescription(listSong.get(i).getSongName() + " - " + listSong.get(i).getSongSinger())
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, listSong.get(i).getSongName() + ".mp3");

                enqueue = downloadManager.enqueue(request);

                Intent intent = new Intent();
                intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                context.startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Touch", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("songName", listSong.get(i).getSongName());
                intent.putExtra("songSinger", listSong.get(i).getSongSinger());
                intent.putExtra("songURL", listSong.get(i).getSongURL());
                context.startActivity(intent);
            }
        });
        return view;
    }

    public void addItem(Song song) {
        if (listSong != null && song != null) {
            listSong.add(song);
        }
    }
}
