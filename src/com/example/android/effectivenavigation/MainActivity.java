/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.effectivenavigation;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.android.effectivenavigation.DatabaseHelper.ALL_COLUMNS;
import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_COLUMN_ORDER;
import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_COLUMN_TASKNAME;
import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_COLUMN_TASKTYPE;
import static com.example.android.effectivenavigation.TaskContentProvider.CONTENT_URI;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	TaskPagerAdapter taskPagerAdapter;
	ViewPager mViewPager;
	int currentTabPosition = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		taskPagerAdapter = new TaskPagerAdapter(getSupportFragmentManager(), this);

		final ActionBar actionBar = actionBar();
		viewPagerWith(actionBar);

		for (int i = 0; i < taskPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(taskPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	private void viewPagerWith(final ActionBar actionBar) {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(taskPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
	}

	private ActionBar actionBar() {
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		return actionBar;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
		currentTabPosition = tab.getPosition() + 1;
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			addTask();
			return true;
		case R.id.action_settings:
			toastMessage("settings");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addTask() {
		if (currentTabPosition == 2) {
			toastMessage("Not allowed");
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Task Name");

		// Set up the input
		final EditText input = new EditText(this);
		input.setInputType(TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Task task = new Task();
				task.setName(input.getText().toString());
				ContentResolver contentResolver = getContentResolver();
				ContentValues values = new ContentValues();
				values.put(TASKS_TABLE_COLUMN_TASKNAME, task.getName());
				values.put(TASKS_TABLE_COLUMN_TASKTYPE, 1);
				values.put(TASKS_TABLE_COLUMN_ORDER, 1);
				Cursor availableTasks = contentResolver.query(CONTENT_URI, ALL_COLUMNS,
						TASKS_TABLE_COLUMN_TASKNAME + "=? AND " + TASKS_TABLE_COLUMN_TASKTYPE + "=?",
						new String[] { task.getName(), "1" }, null);
				if (availableTasks == null) {
					toastMessage("Problem in adding");
				} else if (availableTasks.getCount() == 0) {
					contentResolver.insert(CONTENT_URI, values);
				} else {
					toastMessage("Already exists");
				}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();
	}

	private void toastMessage(String message) {
		Toast toast = Toast.makeText(this, message, LENGTH_SHORT);
		toast.show();
	}

}
