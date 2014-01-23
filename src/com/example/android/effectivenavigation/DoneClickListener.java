package com.example.android.effectivenavigation;

import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_COLUMN_TASKNAME;
import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_COLUMN_TASKTYPE;
import static com.example.android.effectivenavigation.TaskContentProvider.CONTENT_URI;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.android.effectivenavigation.DatabaseHelper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.view.View;
import android.view.View.OnClickListener;

public class DoneClickListener implements OnClickListener {
	@Override
	public void onClick(View buttonView) {
		String[] taskNameArgument = TaskNameHelper.instance().taskNameAsArrayFrom(buttonView);
		ContentResolver contentResolver = buttonView.getContext().getContentResolver();
		contentResolver.update(CONTENT_URI, doneContentValue(), TASKS_TABLE_COLUMN_TASKNAME + "=?",	taskNameArgument);
	}

	private ContentValues doneContentValue() {
		ContentValues values = new ContentValues();
		values.put(TASKS_TABLE_COLUMN_TASKTYPE, 2);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		values.put(DatabaseHelper.TASKS_TABLE_COLUMN_ACHIEVED_DATE, dateFormat.format(new Date().getTime()));
		return values;
	}

}
