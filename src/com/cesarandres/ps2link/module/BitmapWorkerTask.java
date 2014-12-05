package com.cesarandres.ps2link.module;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cesarandres.ps2link.ApplicationPS2Link;

/**
 * This async task will receive resize an image to a size that will fit inside
 * the screen for the device in both horizontal and vertical orientation. The
 * resulting image will be set as the background for the provided view
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private Activity context;
    private String data;
    private static final String DOWNLOAD_DIR = ".cache";
    private static final String PUBLIC_DIR = "AuraxisControlCenter";

    /**
     * @param imageView
     *            View where to where the image will be set after resizing
     * @param context
     *            Activity that requested this task
     */
    public BitmapWorkerTask(ImageView imageView, Activity context) {
	// Use a WeakReference to ensure the ImageView can be garbage
	// collected
	this.imageViewReference = new WeakReference<ImageView>(imageView);
	this.context = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected Bitmap doInBackground(String... params) {
	data = params[0];

	File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), PUBLIC_DIR + File.separator + DOWNLOAD_DIR);
	if (!mediaStorageDir.exists()) {
	    if (!mediaStorageDir.mkdirs()) {
		return null;
	    }
	}

	File file = new File(mediaStorageDir.getAbsoluteFile() + File.separator + data);
	if (!file.exists()) {
	    Display display = context.getWindowManager().getDefaultDisplay();
	    Point size = new Point();
	    int width, height;
	    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
		display.getSize(size);
		width = size.x;
		height = size.y;
	    } else {
	    	Point psize = new Point();
	    	width = display.getWidth();
	    	height = display.getHeight();
	    }

	    InputStream ims;
	    try {
		if (isCancelled()) {
		    return null;
		}
		ims = context.getAssets().open(data);
		Bitmap image = decodeSampledBitmapFromResource(ims, width, height);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 85, bytes);

		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes.toByteArray());
		fos.close();
		ApplicationPS2Link.setBackground(image);
		return image;
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	    }
	} else {
	    try {
		if (isCancelled()) {
		    return null;
		}
		Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
		ApplicationPS2Link.setBackground(image);
		return image;
	    } catch (OutOfMemoryError e) {
		System.gc();
		return null;
	    }
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
	if (!isCancelled() || (imageViewReference != null && bitmap != null)) {
	    final ImageView imageView = imageViewReference.get();
	    if (imageView != null) {
		imageView.setImageBitmap(bitmap);
		imageView.setScaleType(ScaleType.CENTER_CROP);
	    }
	}
    }

    /**
     * @param options
     *            Set of options to apply to the image transformation
     * @param reqWidth
     *            width of the resulting image
     * @param reqHeight
     *            height of the resulting image
     * @return the biggest ratio for the given size
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	// Raw height and width of image
	final int height = options.outHeight;
	final int width = options.outWidth;
	int inSampleSize = 1;

	if (height > reqHeight || width > reqWidth) {

	    // Calculate ratios of height and width to requested height and
	    // width
	    final int heightRatio = Math.round((float) height / (float) reqHeight);
	    final int widthRatio = Math.round((float) width / (float) reqWidth);

	    // Choose the smallest ratio as inSampleSize value, this will
	    // guarantee
	    // a final image with both dimensions larger than or equal to the
	    // requested height and width.
	    inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	}

	return inSampleSize;
    }

    /**
     * @param ims
     *            Stream from the image or resource to resize
     * @param reqWidth
     *            resulting image width
     * @param reqHeight
     *            resulting image height
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(InputStream ims, int reqWidth, int reqHeight) {

	// First decode with inJustDecodeBounds=true to check dimensions
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeStream(ims, null, options);

	// Calculate inSampleSize
	options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	// Decode bitmap with inSampleSize set
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeStream(ims, null, options);
    }
}
