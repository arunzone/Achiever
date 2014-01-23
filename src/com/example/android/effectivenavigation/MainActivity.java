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

import com.example.android.effectivenavigation.R;
import com.example.android.effectivenavigation.Task;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),this);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between different app sections, select the
				// corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if we have
				// a reference to the
				// Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentStatePagerAdapter {

		private Context context;
		public AppSectionsPagerAdapter(FragmentManager fm, Context context) {
			super(fm);
			this.context = context;
		}

		@Override
		public Fragment getItem(int i) {
				Bundle bundle = new Bundle();
				bundle.putInt("taskType", i+1);
				return TaskListFragment.instantiate(context, TaskListFragment.class.getName(), bundle);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Section " + (position + 1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_add:
			addTask();
			toastMessage("add a task");
			return true;
		case R.id.action_settings:
			toastMessage("settings");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addTask() {
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
