package com.example.android.effectivenavigation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "achieve";
	public static final String TASKS_TABLE_NAME = "tasks";
	public static final String TASKS_TABLE_COLUMN_ID = "_id";
	public static final String TASKS_TABLE_COLUMN_TASKNAME = "taskName";
	public static final String TASKS_TABLE_COLUMN_TASKTYPE = "taskType";
	public static final String TASKS_TABLE_COLUMN_ORDER = "taskOrder";
	public static final String TASKS_TABLE_COLUMN_UPDATE_DATE = "updatedDate";
	public static final String TASKS_TABLE_COLUMN_ACHIEVED_DATE = "achievedDate";
	public static final String TASKS_TABLE_CREATE = TASKS_TABLE_NAME + " (" 
			+ TASKS_TABLE_COLUMN_ID	+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
			+ TASKS_TABLE_COLUMN_TASKNAME + " TEXT NOT NULL, "
			+ TASKS_TABLE_COLUMN_ORDER + " NUMERIC, " 
			+ TASKS_TABLE_COLUMN_TASKTYPE + " NUMERIC, "
			+ TASKS_TABLE_COLUMN_UPDATE_DATE +" DATETIME DEFAULT current_timestamp, "
			+ TASKS_TABLE_COLUMN_ACHIEVED_DATE +" DATETIME );";
	private static final int DATABASE_VERSION = 2;
	public static final String[] ALL_COLUMNS = { TASKS_TABLE_COLUMN_ID, TASKS_TABLE_COLUMN_TASKNAME,
			TASKS_TABLE_COLUMN_ORDER, TASKS_TABLE_COLUMN_TASKTYPE };

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TASKS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*
		 * // Here you can perform updates when the database structure changes
		 * // Begin transaction db.beginTransaction();
		 * 
		 * try { if (oldVersion < 2) { // Upgrade database structure from
		 * Version 1 to 2 String alterTable = "ALTER ....";
		 * 
		 * db.execSQL(alterTable); } // This allows you to upgrade from any
		 * version to the next most // recent one in multiple steps as you don't
		 * know if the user has // skipped any of the previous updates if
		 * (oldVersion < 3) { // Upgrade database structure from Version 2 to 3
		 * String alterTable = "ALTER ....";
		 * 
		 * db.execSQL(alterTable); }
		 * 
		 * // Only when this code is executed, the changes will be applied // to
		 * the database db.setTransactionSuccessful(); } catch (Exception ex) {
		 * ex.printStackTrace(); } finally { // Ends transaction // If there was
		 * an error, the database won't be altered db.endTransaction(); }
		 */
	}
}