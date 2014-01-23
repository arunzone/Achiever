package com.example.android.effectivenavigation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TaskPagerAdapter extends FragmentStatePagerAdapter {
	private Context context;
	
	public TaskPagerAdapter(FragmentManager fm, Context context) {
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
		return position==0? "To do":"Done";
	}
}

