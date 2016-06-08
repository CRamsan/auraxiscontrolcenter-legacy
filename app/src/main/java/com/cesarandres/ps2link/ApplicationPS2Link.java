package com.cesarandres.ps2link;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.volley.BitmapLruCache;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class ApplicationPS2Link extends Application {

    static final String ACTIVITY_MODE_KEY = "activity_mode";
    private static final int MAX_CACHE_SIZE = 2000000; //2 MB
    public static RequestQueue volley;
    public static ImageLoader mImageLoader;
    private static Bitmap background;
    private static WallPaperMode wallpaper = WallPaperMode.PS2;

    /**
     * @return the current Wallpaper mode
     */
    public static WallPaperMode getWallpaperMode() {
        return wallpaper;
    }

    /**
     * @param wallpaper the wallpaper mode that matches the current wallpaper. This
     *                  method should only be called after the wallpaper has been set
     */
    public static void setWallpaperMode(WallPaperMode wallpaper) {
        ApplicationPS2Link.wallpaper = wallpaper;
    }

    /**
     * @return the bitmap holding the data for the background for all activities
     */
    public static Bitmap getBackground() {
        return background;
    }

    /**
     * @param background bitmap that will be used as background for all activities
     */
    public static void setBackground(Bitmap background) {
        ApplicationPS2Link.background = background;
    }

    /**
     * @return the size in bytes of all the files in the cache
     */
    public static long getCacheSize(Context context) {
        String[] fileList = context.fileList();
        long size = 0;
        for (String file : fileList) {
            size += new File(file).length();
        }
        return size;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();


        if (volley == null) {
            ApplicationPS2Link.volley = Volley.newRequestQueue(this);
        }
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(ApplicationPS2Link.volley,
                    new BitmapLruCache());
        }

        if (getCacheSize(this) > MAX_CACHE_SIZE) {
            new ClearCache().execute();
        }

        String lang = Locale.getDefault().getLanguage();
        DBGCensus.currentLang = DBGCensus.CensusLang.EN;
        for (DBGCensus.CensusLang clang : DBGCensus.CensusLang.values()) {
            if (lang.equalsIgnoreCase(clang.name())) {
                DBGCensus.currentLang = clang;
            }
        }
    }

    /**
     * This enum holds all the different activity modes, there is one for each
     * main fragment that is used.
     */
    public static enum ActivityMode {
        ACTIVITY_ADD_OUTFIT,
        ACTIVITY_ADD_PROFILE,
        ACTIVITY_MEMBER_LIST,
        ACTIVITY_OUTFIT_LIST,
        ACTIVITY_PROFILE,
        ACTIVITY_PROFILE_LIST,
        ACTIVITY_SERVER_LIST,
        ACTIVITY_TWITTER,
        ACTIVITY_LINK_MENU,
        ACTIVITY_MAIN_MENU,
        ACTIVITY_REDDIT,
        ACTIVITY_ABOUT,
        ACTIVITY_SETTINGS
    }


    /**
     * This enum holds the reference for all four background types.
     */
    public static enum WallPaperMode {
        PS2, NC, TR, VS
    }

    /**
     * This AsyncTask will delete all the files from the cache.
     */
    public class ClearCache extends AsyncTask<String, Integer, Boolean> {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... args) {
            String[] fileList = fileList();
            for (String file : fileList) {
                deleteFile(file);
            }
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Boolean result) {

        }
    }

    /**
     * This AsyncTask will delete the least used files from the cache until it reaches its maximum size allowed.
     */
    public class TrimCache extends AsyncTask<String, Integer, Boolean> {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... args) {

            File[] files = getFilesDir().listFiles();

            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                }
            });

            int i = 0;
            while (getCacheSize(getApplicationContext()) > MAX_CACHE_SIZE && i < files.length) {
                deleteFile(files[i].getAbsolutePath());
                i++;
            }

            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Boolean result) {

        }
    }
}