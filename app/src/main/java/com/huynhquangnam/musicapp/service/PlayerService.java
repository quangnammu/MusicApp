package com.huynhquangnam.musicapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.huynhquangnam.musicapp.R;

import java.io.File;
import java.io.IOException;

public class PlayerService extends Service {

    Thread t;
    Handler h;
    MediaPlayer mediaPlayer;
    String url;
    String name;
    UpdateTimerRunnable updateTimerRunnable;
    NotificationManager notificationManager;
    BroadcastReceiver notificationReceiver;

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra("songURL");
        name = intent.getStringExtra("songName");
        playAudio();
        showNotification();
        IntentFilter filter = new IntentFilter("changeStatusMedia");
        filter.addAction("seekChange");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver(), filter);

        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "changeStatus":
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            updateTimerRunnable.onPause();
                        } else {
                            mediaPlayer.start();
                            updateTimerRunnable.onResume();
                        }
                        break;
                    case "stopAudio":
                        mediaPlayer.stop();
                        updateTimerRunnable.onPause();
                        notificationManager.cancelAll();
                        break;
                }
            }
        };

        IntentFilter filter = new IntentFilter("changeStatus");
        registerReceiver(notificationReceiver, filter);

        PendingIntent changeStatusIntent = PendingIntent.getBroadcast(this, 0, new Intent("changeStatus"), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent stopAudio = PendingIntent.getBroadcast(this, 0, new Intent("stopAudio"), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Audio Player")
                .setContentText(name)
                .addAction(R.drawable.ic_download, "pause/resume", changeStatusIntent)
                .addAction(R.drawable.ic_download, "stop audio", stopAudio)
                .setSmallIcon(R.drawable.ic_download).build();

        startForeground(1, notification);
    }

    private BroadcastReceiver receiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "changeStatusMedia":
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            updateTimerRunnable.onPause();
                        } else {
                            mediaPlayer.start();
                            updateTimerRunnable.onResume();
                        }
                        break;
                    case "seekChange":
                        int currentPosition = intent.getIntExtra("currentPosition", 0);
                        mediaPlayer.seekTo(currentPosition * mediaPlayer.getDuration() / 100);
                }
            }
        };
    }

    private void sendDurationToActivity(int duration) {
        Intent intent = new Intent("sendDuration");
        intent.putExtra("duration", duration);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void sendProgressToActivity(int progress, int currentPosition) {
        Intent intent = new Intent("sendProgress");
        intent.putExtra("progress", progress);
        intent.putExtra("currentPosition", currentPosition);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    class UpdateTimerRunnable implements Runnable {

        private Object pauseLock;
        private boolean isPause;

        public UpdateTimerRunnable() {
            isPause = false;
            pauseLock = new Object();
        }

        @Override
        public void run() {
            while (mediaPlayer.isPlaying()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendProgressToActivity(mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getCurrentPosition(), mediaPlayer.getCurrentPosition());
                synchronized (pauseLock) {
                    while (isPause) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public void onPause() {
            synchronized (pauseLock) {
                isPause = true;
            }
        }

        public void onResume() {
            synchronized (pauseLock) {
                isPause = false;
                pauseLock.notifyAll();
            }
        }
    }

    private void createUpdateTimer() {
        updateTimerRunnable = new UpdateTimerRunnable();
        new Thread(updateTimerRunnable).start();
    }

    private void playAudio() {
        if (h == null) {
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    h = new Handler();
                    File f = new File(url);
                    if (f.exists() && f.isFile()) {
                        h.post(playAudioFromFile());
                    } else {
                        h.post(playAudioFromUrl());
                    }
                    Looper.loop();
                }
            });
            t.start();
        } else {
            File f = new File(url);
            if (f.exists() && f.isFile()) {
                h.post(playAudioFromFile());
            } else {
                h.post(playAudioFromUrl());
            }
        }

    }

    private Runnable playAudioFromFile() {
        return new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = new MediaPlayer();
                File f = new File(url);
                if (!f.exists()) {
                    stopSelf();
                    return;
                }

                try {
                    mediaPlayer.setDataSource(f.getPath());
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            sendDurationToActivity(mediaPlayer.getDuration());
                            createUpdateTimer();
                        }
                    });
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        };

    }

    private Runnable playAudioFromUrl() {
        return new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            sendDurationToActivity(mediaPlayer.getDuration());
                            createUpdateTimer();
                        }
                    });
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
            }
        };
    }
}
