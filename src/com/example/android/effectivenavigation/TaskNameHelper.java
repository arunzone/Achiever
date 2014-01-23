package com.example.android.effectivenavigation;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.android.effectivenavigation.R;

public class TaskNameHelper {
	private static TaskNameHelper helper;

	public static TaskNameHelper instance() {
		if (helper == null) {
			helper = new TaskNameHelper();
		}
		return helper;
	}

	private TaskNameHelper() {
	}

	public String[] taskNameAsArrayFrom(View buttonView) {
		String taskName = taskNameFrom(buttonView);
		String[] taskNameArgument = { taskName };
		return taskNameArgument;
	}

	private String taskNameFrom(View buttonView) {
		FrameLayout frameLayout = (FrameLayout) buttonView.getParent();
		TextView taskTextView = (TextView) frameLayout.findViewById(R.id.textView1);
		String taskName = taskTextView.getText().toString();
		return taskName;
	}
}
