package com.cjvilla.yml.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class PersonImageCache extends LruCache<String,Bitmap> {

	private static PersonImageCache personImageCache;
	public static PersonImageCache instance() {
		if (personImageCache==null) {
			int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			final int cacheSize = maxMemory / 4;
			personImageCache=new PersonImageCache(cacheSize);
		}
		return personImageCache;
	}
	private PersonImageCache(int maxSize) {
		super(maxSize);
	}

	public void addBitmapToCache(String key, Bitmap bitmap) {
		if (getCacheBitmap(key) == null) {
			personImageCache.put(key, bitmap);
		}
	}

	public Bitmap getCacheBitmap(String key) {
		return personImageCache.get(key);
	}
	/** The cache size will be measured in kilobytes rather than
	* number of items.
	 * */
	@Override
	protected int sizeOf(String key, Bitmap bitmap) {

		return bitmap.getByteCount() / 1024;
	}
}
