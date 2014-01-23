package com.example.android.effectivenavigation;

import static android.view.View.INVISIBLE;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageButton;

import com.example.android.effectivenavigation.DeleteClickListener;
import com.example.android.effectivenavigation.DoneClickListener;

public class TaskCursorAdapter extends SimpleCursorAdapter {

	int[] colors;
	public TaskCursorAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to) {
		super(context, layout, cursor, from, to, 0);
		colors = context.getResources().getIntArray(R.array.rainbow);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		setupDoneButton(view);
		setupDeleteButton(view);
		view.setBackgroundColor(colors[cursor.getPosition()%15]);
	}

	private void setupDoneButton(View view) {
		ImageButton doneButton = (ImageButton) view.findViewById(R.id.done);
		doneButton.setVisibility(INVISIBLE);
		doneButton.setOnClickListener(new DoneClickListener());
	}
	private void setupDeleteButton(View view) {
		ImageButton doneButton = (ImageButton) view.findViewById(R.id.delete);
		doneButton.setVisibility(INVISIBLE);
		doneButton.setOnClickListener(new DeleteClickListener());
	}

}
