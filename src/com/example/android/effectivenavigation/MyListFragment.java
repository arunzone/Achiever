package com.example.android.effectivenavigation;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.android.effectivenavigation.DatabaseHelper.ALL_COLUMNS;
import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_COLUMN_TASKNAME;
import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_COLUMN_TASKTYPE;
import static com.example.android.effectivenavigation.TaskContentProvider.CONTENT_URI;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class MyListFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	private TaskCursorAdapter taskCursorAdapter;
	private View currentSelectedView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		taskCursorAdapter = new TaskCursorAdapter(getActivity(), R.layout.task_items, null,
				new String[] { TASKS_TABLE_COLUMN_TASKNAME }, new int[] { R.id.textView1 });
		setListAdapter(taskCursorAdapter);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		if (currentSelectedView != null && currentSelectedView != view) {
			hideAdditionalFeatureFor(currentSelectedView);
		}

		currentSelectedView = view;
		showAdditionalFeatureFor(currentSelectedView);
	}

	private void showAdditionalFeatureFor(View view) {
		((ImageButton) view.findViewById(R.id.delete)).setVisibility(VISIBLE);
		((ImageButton) view.findViewById(R.id.done)).setVisibility(VISIBLE);
	}

	private void hideAdditionalFeatureFor(View view) {
		((ImageButton) view.findViewById(R.id.delete)).setVisibility(INVISIBLE);
		((ImageButton) view.findViewById(R.id.done)).setVisibility(INVISIBLE);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(getActivity(), CONTENT_URI, ALL_COLUMNS, TASKS_TABLE_COLUMN_TASKTYPE + "=1",
				null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		taskCursorAdapter.swapCursor(data);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		taskCursorAdapter.swapCursor(null);

	}
}