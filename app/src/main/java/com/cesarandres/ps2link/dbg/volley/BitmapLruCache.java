package com.cesarandres.ps2link.dbg.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Bitmap Least-Recently-Used cache. It is basically a queue. Old entries get
 * deleted when the cache is full. If a entry is accessed it is moved to the
 * front of the queue
 */
public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {

    /**
     * Create the LRU cache with the default size
     */
    public BitmapLruCache() {
        this(getDefaultLruCacheSize());
    }

    /**
     * @param sizeInKiloBytes size in KB for the new cache
     */
    public BitmapLruCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    /**
     * The size of the cache is based on the memory available
     *
     * @return the size in bytes for the cache
     */
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    /* (non-Javadoc)
     * @see android.support.v4.util.LruCache#sizeOf(java.lang.Object, java.lang.Object)
     */
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    /* (non-Javadoc)
     * @see com.android.volley.toolbox.ImageLoader.ImageCache#getBitmap(java.lang.String)
     */
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    /* (non-Javadoc)
     * @see com.android.volley.toolbox.ImageLoader.ImageCache#putBitmap(java.lang.String, android.graphics.Bitmap)
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}