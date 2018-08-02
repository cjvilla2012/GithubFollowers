package com.cjvilla.yml.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.View;

import com.cjvilla.yml.R;
import com.cjvilla.yml.cache.PersonImageCache;
import com.cjvilla.yml.databinding.ActivityPersonBinding;
import com.cjvilla.yml.model.GithubPerson;
import com.cjvilla.yml.net.GetGithubPerson;

public class DisplayPersonActivity extends BaseYMLActivity implements GetGithubPerson.CallMe{
	private static final String TAG="DisplayPersonActivity";
	private static final String KEY_PERSON="person";
	private static final String EXTRA_IMAGE = "imageTransitionTarget";
	private ActivityPersonBinding binding;
	private GetGithubPerson getGithubPerson;

	public static void navigate(AppCompatActivity activity, View transitionImage, String person) {
		Intent intent = new Intent(activity, DisplayPersonActivity.class);
		intent.putExtra(KEY_PERSON, person);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage,
				activity.getString(R.string.transition));
		ActivityCompat.startActivity(activity, intent, options.toBundle());
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		binding = DataBindingUtil.setContentView(this, R.layout.activity_person);
		super.onCreate(savedInstanceState);
		createToolbar(true);
		showTitle(null);
		String person=getIntent().getStringExtra(KEY_PERSON);
		getGithubPerson=new GetGithubPerson(person,this);
		getGithubPerson.execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (getGithubPerson!=null) {
			Log.d(TAG,"cancel GetGithubPerson");
			getGithubPerson.cancel(true);
			getGithubPerson=null;
		}
	}

	private void initActivityTransitions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Slide transition = new Slide();
			transition.excludeTarget(android.R.id.statusBarBackground, true);
			getWindow().setEnterTransition(transition);
			getWindow().setReturnTransition(transition);
		}
	}

	@Override
	public void setPerson(GithubPerson person) {
		binding.setViewModel(person);
		binding.image.setImageBitmap(PersonImageCache.instance().getCacheBitmap(person.getLogin()));
	}
}
