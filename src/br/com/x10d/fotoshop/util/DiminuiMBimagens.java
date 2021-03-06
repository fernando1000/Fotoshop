package br.com.x10d.fotoshop.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DiminuiMBimagens {

	
	public static Bitmap decodeSampledBitmapFromPicturePath(String picturePath, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		// decode with inJustDecodeBounds=true to check dimensions
		options.inJustDecodeBounds = true;
		
		Bitmap aa = BitmapFactory.decodeFile(picturePath, options);
		
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		
		Bitmap bb = BitmapFactory.decodeFile(picturePath, options);
		
		return bb;
		
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		
		BitmapFactory.decodeResource(res, resId, options);
		
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);	
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeResource(res, resId, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}


}
