package com.cjvilla.yml.ui.adapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cjvilla.yml.cache.PersonImageCache;
import com.cjvilla.yml.databinding.ItemRecyclerBinding;
import com.cjvilla.yml.model.GithubPerson;
import com.cjvilla.yml.model.VMHandlers;
import com.cjvilla.yml.net.GetGithubImage;

import java.util.ArrayList;
import java.util.List;

public class GithubPersonAdapter extends RecyclerView.Adapter<GithubPersonAdapter.GithubPersonViewHolder> {
	private static final String TAG="GithubPersonAdapter";
	private AppCompatActivity activity;
	private List<GithubPerson> items;
	private VMHandlers vmHandler;
	private ArrayList<GetGithubImage> imageTasks=new ArrayList<>();

	public GithubPersonAdapter(AppCompatActivity activity,List<GithubPerson> items) {
		this.activity=activity;
		this.items = items;
		vmHandler=new VMHandlers(activity);
	}

	@NonNull
	@Override
	public GithubPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater =
				LayoutInflater.from(parent.getContext());
		ItemRecyclerBinding itemBinding =
				ItemRecyclerBinding.inflate(layoutInflater, parent, false);
		return new GithubPersonViewHolder(itemBinding);
	}

	@Override
	public void onBindViewHolder(@NonNull GithubPersonViewHolder holder, int position) {
		if (position < items.size()) {
			holder.bind(items.get(position));
		}
	}

	@Override
	public int getItemCount() {
		return items == null ? 0 : items.size();
	}


	/** Call this from onDestroy of the parent to stop running task.
	 *
	 */
	public void cancel() {
		for(GetGithubImage task:imageTasks) {
			Log.d(TAG,"cancel task for:"+task.getImageUrl());
			task.cancel(true);
		}
		imageTasks.clear();
	}

	private boolean loadBitmap(String key, ImageView imageView) {
		final Bitmap bitmap = PersonImageCache.instance().getCacheBitmap(key);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			return true;
		}
		return false;
	}

	protected class GithubPersonViewHolder extends RecyclerView.ViewHolder {
		private final ItemRecyclerBinding binding;

		public GithubPersonViewHolder(ItemRecyclerBinding binding) {
			super(binding.getRoot());
			this.binding=binding;
		}

		public void bind(final GithubPerson item) {
			binding.setViewModel(item);
			binding.setViewHandler(vmHandler);
			if (!loadBitmap(item.getLogin(),binding.image)) {
				//TODO Get smarter about removing task from the Task list once they complete
				GetGithubImage task=new GetGithubImage(item.getAvatar_url(), new GetGithubImage.ImageRetrieved() {
					@Override
					public void setBitmap(Bitmap bitmap) {
						if (!activity.isDestroyed()) {
							PersonImageCache.instance().addBitmapToCache(item.getLogin(), bitmap);
							binding.image.setImageBitmap(bitmap);
						}
					}
				});
				imageTasks.add(task);
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			binding.executePendingBindings();
		}
	}
}
