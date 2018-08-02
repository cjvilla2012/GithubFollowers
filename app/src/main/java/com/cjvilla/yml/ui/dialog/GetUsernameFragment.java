package com.cjvilla.yml.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjvilla.yml.R;

public class GetUsernameFragment extends DialogFragment {

	public interface GithubSearch {
		void performSearch(String person);
	}

	public static GetUsernameFragment newInstance() {
		return new GetUsernameFragment();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final EditText input = new EditText(getActivity());
		input.setSingleLine(true);
		input.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
				String person = input.getText().toString().trim();
				if (!TextUtils.isEmpty(person)) {
					dismiss();
					((GithubSearch) getActivity()).performSearch(person);
					return true;
				}
				return false;
			}
		});
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		builder.setView(input);
		builder.setMessage(R.string.search_hint)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String person = input.getText().toString().trim();
						if (!TextUtils.isEmpty(person)) {
							((GithubSearch) getActivity()).performSearch(person);
						}
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});

		return builder.create();
	}
}
