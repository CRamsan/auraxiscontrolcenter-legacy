package com.cesarandres.ps2link.dbg.util;

import android.util.Log;

/**
 * Just a wrapper over the Log class provided by android
 */
public class Logger {

    /**
     * @param level   logging level
     * @param tag     will be used as a tag for the message
     * @param message actual message
     */
    public static final void log(int level, Object tag, String message) {
        Logger.log(level, tag.getClass().getName(), message);
    }

    /**
     * @param level   logging level
     * @param tag     will be used as a tag for the message
     * @param message actual message
     */
    public static final void log(int level, String tag, String message) {
        switch (level) {
            case Log.INFO:
                Log.i(tag, message);
                break;
            case Log.ASSERT:
                Log.wtf(tag, message);
                break;
            case Log.ERROR:
                Log.e(tag, message);
                break;
            case Log.VERBOSE:
                Log.v(tag, message);
                break;
            case Log.DEBUG:
                Log.d(tag, message);
                break;
            case Log.WARN:
                Log.w(tag, message);
            default:
                Log.w(tag, "EXCEPECTED DEBUG LEVEL:-" + message);
                break;
        }
    }

}
