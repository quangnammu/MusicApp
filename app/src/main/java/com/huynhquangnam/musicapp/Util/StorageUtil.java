package com.huynhquangnam.musicapp.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

public class StorageUtil {
    public static Cursor getMP3FileCursor(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA
        };
        return contentResolver.query(uri, projection, selection, null, null);

    }

    public static Bitmap getCoverPicture(Context context, Uri uri) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        BitmapFactory.Options bfo = new BitmapFactory.Options();

        mmr.setDataSource(context, uri);
        rawArt = mmr.getEmbeddedPicture();
        if (null != rawArt) {
            return BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        }

        return null;
    }
}
