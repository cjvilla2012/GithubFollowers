package com.cjvilla.yml.net;

import android.os.AsyncTask;
import android.util.Log;

import com.cjvilla.yml.model.GithubPerson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** Get up to MAX_FOLLOWERS for the Github user. This does not implementation pagination,
 * meaning the user may have more than that number.
 */
public class GetGithubPeople extends AsyncTask<String, String, String> {
	private static final String TAG="GetGithubPeople";
	private static final int MAX_FOLLOWERS=66;
	public interface CallMe {
		void setItems(List<GithubPerson> items);
	}

	private CallMe callMe;
	private String person;
	HttpURLConnection urlConnection;

	public GetGithubPeople(String person,CallMe callme) {
		this.person=person;
		this.callMe = callme;
	}

	@Override
	protected String doInBackground(String... args) {

		StringBuilder result = new StringBuilder();

		try {
			URL url = new URL("https://api.github.com/users/"+person+"/followers?per_page="+MAX_FOLLOWERS);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while (!isCancelled() && (line = reader.readLine()) != null) {
				result.append(line);
			}

		} catch (Exception e) {
			//We may get exceptions here from cancelling the task. We don't care
			if (!isCancelled()) {
				Log.e(TAG, "Exception reading people:" + e.getMessage());
			}
		} finally {
			urlConnection.disconnect();
		}
		return result.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		if (result.length() > 0) {
			try {
				JSONArray followers = new JSONArray(result);
				ArrayList<GithubPerson> items = new ArrayList<>();
				for (int i = 0; i < followers.length(); i++) {
					items.add(new GithubPerson(followers.getJSONObject(i)));
				}
				callMe.setItems(items);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			//Exceptions return a 0-length list.
			callMe.setItems(new ArrayList<GithubPerson>());
		}
	}

}