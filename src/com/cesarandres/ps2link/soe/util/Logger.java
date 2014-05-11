package com.cesarandres.ps2link.soe.util;

import android.util.Log;

public class Logger {

    public static final void log(int level, Object object, String message) {
	Logger.log(level, object.getClass().getName(), message);
    }
    
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
