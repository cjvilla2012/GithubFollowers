package com.cjvilla.yml.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetGithubImage extends AsyncTask<String, String, Bitmap> {
	private static final String TAG="GetGithubImage";
	public interface ImageRetrieved {
		void setBitmap(Bitmap bitmap);
	}
	private ImageRetrieved imageRetrieved;
	private String imageUrl;
	public String getImageUrl() {return imageUrl;}
	private HttpURLConnection urlConnection;

	public GetGithubImage(String imageUrl,ImageRetrieved imageRetrieved) {
		this.imageUrl=imageUrl;
		this.imageRetrieved=imageRetrieved;
	}

	@Override
	protected Bitmap doInBackground(String... args) {

		Bitmap bitmap=null;
		try {
			Log.d(TAG,"get "+imageUrl);
			URL url = new URL(imageUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.connect();
			InputStream input = urlConnection.getInputStream();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			if (!isCancelled()) {
				bitmap = BitmapFactory.decodeStream(input);
			}
		}catch(OutOfMemoryError error) {
			//TODO Downscale image and try again
		}catch(Exception e) {
			//We may get exceptions here from cancelling the task. We don't care
			if (!isCancelled()) {
				Log.e(TAG, "Exception reading image " + imageUrl + ":" + e.getMessage());
			}
		}
		finally {
			urlConnection.disconnect();
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (!isCancelled()) {
			try {
				imageRetrieved.setBitmap(bitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}