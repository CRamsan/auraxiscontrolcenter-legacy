package com.cesarandres.ps2link.soe.util;

import android.util.Log;

public class Logger {

    public static final void log(int level, Object object, String message) {
	switch (level) {
	case Log.INFO:
	    Log.i(object.getClass().getName(), message);
	    break;
	case Log.ASSERT:
	    Log.wtf(object.getClass().getName(), message);
	    break;
	case Log.ERROR:
	    Log.e(object.getClass().getName(), message);
	    break;
	case Log.VERBOSE:
	    Log.v(object.getClass().getName(), message);
	    break;
	case Log.DEBUG:
	    Log.d(object.getClass().getName(), message);
	    break;
	case Log.WARN:
	    Log.w(object.getClass().getName(), message);
	default:
	    Log.w(object.getClass().getName(), "EXCEPECTED DEBUG LEVEL:-" + message);
	    break;
	}
    }

}
