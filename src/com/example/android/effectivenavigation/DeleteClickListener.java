package com.example.android.effectivenavigation;

import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_COLUMN_TASKNAME;
import static com.example.android.effectivenavigation.TaskContentProvider.CONTENT_URI;
import android.content.ContentResolver;
import android.view.View;
import android.view.View.OnClickListener;

public class DeleteClickListener implements OnClickListener {
	@Override
	public void onClick(View buttonView) {
		String[] taskNameArgument = TaskNameHelper.instance().taskNameAsArrayFrom(buttonView);
		ContentResolver contentResolver = buttonView.getContext().getContentResolver();
		contentResolver.delete(CONTENT_URI, TASKS_TABLE_COLUMN_TASKNAME + "=?", taskNameArgument);
	}
}
