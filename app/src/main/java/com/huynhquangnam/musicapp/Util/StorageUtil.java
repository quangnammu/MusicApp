package com.huynhquangnam.musicapp.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

public class StorageUtil {
    public static Cursor getMP3FileCursor(Context context ){
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mineType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
        String[] selectionArgsMp3 = new String[] {mineType};
        return contentResolver.query(uri, null, selection, selectionArgsMp3, null);

    }
}
