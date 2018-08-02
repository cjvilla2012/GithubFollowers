package com.cjvilla.yml.model;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cjvilla.yml.ui.activity.DisplayPersonActivity;

public class VMHandlers {
	private AppCompatActivity activity;

	public VMHandlers(AppCompatActivity activity) {
		this.activity=activity;
	}

	public void showPerson(View view,GithubPerson person) {
		DisplayPersonActivity.navigate(activity,view,person.getLogin());
	}
}
