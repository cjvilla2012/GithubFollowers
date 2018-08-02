package com.cjvilla.yml.model;

import android.databinding.BaseObservable;

import org.json.JSONException;
import org.json.JSONObject;

public class GithubPerson extends BaseObservable {
	private String login;
	private String avatar_url;
	private String name;
	private String location;
	private String email;
	private int public_repos;
	private int followers;
	private int following;

	public GithubPerson(JSONObject jsonObject) {
		try {
			this.login = getString(jsonObject, "login");
			this.avatar_url=getString(jsonObject, "avatar_url");
			this.name=getString(jsonObject, "name");
			this.location=getString(jsonObject, "location");
			this.email=getString(jsonObject, "email");
			this.public_repos=getInt(jsonObject, "public_repos");
			this.followers=getInt(jsonObject, "followers");
			this.following=getInt(jsonObject, "following");
		} catch (JSONException ex) {
			//TODO In production we don't just do this stack trace
			ex.printStackTrace();
		}
	}

	private String getString(JSONObject jsonObject,String tag) throws JSONException {
		String value=null;
		if (!jsonObject.isNull(tag)) {
			value=jsonObject.getString(tag);
		}
		return value;
	}

	private int getInt(JSONObject jsonObject,String tag) throws JSONException {
		if (jsonObject.has(tag)) {
			return jsonObject.getInt(tag);
		}
		return 0;
	}
	public String getLogin() {
		return login;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getEmail() {
		return email;
	}

	public int getRepositories() {
		return public_repos;
	}

	public int getFollowers() {
		return followers;
	}

	public int getFollowing() {
		return following;
	}

}
