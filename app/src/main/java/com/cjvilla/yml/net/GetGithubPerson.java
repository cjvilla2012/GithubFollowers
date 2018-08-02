package com.cjvilla.yml.net;

import android.os.AsyncTask;
import android.util.Log;

import com.cjvilla.yml.model.GithubPerson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetGithubPerson extends AsyncTask<String, String, String> {
	private static final String TAG="GetGithubPerson";
	public interface CallMe {
		void setPerson(GithubPerson person);
	}
	private String person;
	private CallMe callMe;
	HttpURLConnection urlConnection;

	public GetGithubPerson(String person,CallMe callme) {
		this.person=person;
		this.callMe=callme;
	}
	@Override
	protected String doInBackground(String... args) {

		StringBuilder result = new StringBuilder();
		try {
			URL url = new URL("https://api.github.com/users/"+person);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while (!isCancelled() && (line = reader.readLine()) != null) {
				result.append(line);
			}

		}catch( Exception e) {
			//We may get exceptions here from cancelling the task. We don't care
			if (!isCancelled()) {
				Log.e(TAG, "Exception reading person "+person+":" + e.getMessage());
			}
		}
		finally {
			urlConnection.disconnect();
		}


		return result.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		try {
			GithubPerson person=new GithubPerson(new JSONObject(result));
			callMe.setPerson(person);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}