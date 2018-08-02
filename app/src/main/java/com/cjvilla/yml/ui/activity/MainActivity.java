package com.cjvilla.yml.ui.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.cjvilla.yml.R;
import com.cjvilla.yml.databinding.ActivityMainBinding;
import com.cjvilla.yml.model.GithubPerson;
import com.cjvilla.yml.net.GetGithubPeople;
import com.cjvilla.yml.ui.adapter.GithubPersonAdapter;
import com.cjvilla.yml.ui.dialog.GetUsernameFragment;

import java.util.ArrayList;
import java.util.List;

/** This is the View in MVVM */
public class MainActivity extends BaseYMLActivity implements GetGithubPeople.CallMe,
		GetUsernameFragment.GithubSearch
{
	private static final String TAG="MainActivity";
	private ActivityMainBinding binding;
	private GithubPersonAdapter adapter;
	private List<GithubPerson> items=new ArrayList<>();
	private GetGithubPeople getGithubPeople;

	@Override
	public void setItems(List<GithubPerson> items) {
		showProgress(false);
		this.items = items;
		initRecyclerView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		super.onCreate(savedInstanceState);
		createToolbar(false);
		onNewIntent(getIntent());
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			performSearch(query);
		} else {
			initRecyclerView();
			promptForUser();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (getGithubPeople!=null) {
			Log.d(TAG,"cancel GetGithubPeople");
			getGithubPeople.cancel(true);
			getGithubPeople=null;
		}
		if (adapter!=null) {
			adapter.cancel();
		}
	}

	private void initRecyclerView() {
		adapter = new GithubPersonAdapter(this,items);
		binding.recycler.setAdapter(adapter);
		binding.recycler.setLayoutManager(new GridLayoutManager(this,3));
	}

	public void performSearch(String person) {
		showTitle(String.format(getString(R.string.fmt_home),person));
		showProgress(true);
		getGithubPeople = new GetGithubPeople(person,this);
		getGithubPeople.execute();
	}

	private void promptForUser() {
		GetUsernameFragment.newInstance().show(getFragmentManager(),"dialog");
	}

	private void showProgress(boolean show) {
		if (show) {
			binding.recycler.setVisibility(View.GONE);
			binding.progress.setVisibility(View.VISIBLE);
		} else {
			binding.recycler.setVisibility(View.VISIBLE);
			binding.progress.setVisibility(View.GONE);
		}
	}

}
