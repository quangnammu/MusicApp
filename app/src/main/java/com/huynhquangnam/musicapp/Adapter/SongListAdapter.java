package com.huynhquangnam.musicapp.Adapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.huynhquangnam.musicapp.Activity.PlayerActivity;
import com.huynhquangnam.musicapp.Element.Song;
import com.huynhquangnam.musicapp.R;

import java.util.ArrayList;

public class SongListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    public ArrayList<Song> listSong;
    public Context context;
    DownloadManager downloadManager;
    long enqueue;

    public SongListAdapter(Context context) {
        this.context = context;
        this.listSong = new ArrayList<>();

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
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("songName", listSong.get(i).getSongName());
        intent.putExtra("songURL", listSong.get(i).getSongURL());
        context.startActivity(intent);
    }
}
