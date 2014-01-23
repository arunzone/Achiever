package com.example.android.effectivenavigation;

import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_COLUMN_ID;
import static com.example.android.effectivenavigation.DatabaseHelper.TASKS_TABLE_NAME;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.android.effectivenavigation.DatabaseHelper;

public class TaskContentProvider extends ContentProvider {
	private static final String AUTHORITY = "com.example.android.effectivenavigation";
	private static final String TASKS_TABLE = "tasks";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TASKS_TABLE);

	public static final int TASKS = 1;
	public static final int TASKS_ID = 2;
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	private DatabaseHelper databaseHelper;

	static {
		sURIMatcher.addURI(AUTHORITY, TASKS_TABLE, TASKS);
		sURIMatcher.addURI(AUTHORITY, TASKS_TABLE + "/#", TASKS_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();
		int rowsDeleted = 0;

		switch (uriType) {
		case TASKS:
			rowsDeleted = sqlDB.delete(TASKS_TABLE_NAME, selection, selectionArgs);
			break;

		case TASKS_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(TASKS_TABLE_NAME, TASKS_TABLE_COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(TASKS_TABLE_NAME, TASKS_TABLE_COLUMN_ID + "=" + id + " and "
						+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);

		SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case TASKS:
			id = sqlDB.insert(TASKS_TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri,null);
		return Uri.parse(TASKS_TABLE + "/" + id);
	}

	@Override
	public boolean onCreate() {
		databaseHelper = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TASKS_TABLE_NAME);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TASKS_ID:
			queryBuilder.appendWhere(TASKS_TABLE_COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		case TASKS:
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(databaseHelper.getReadableDatabase(), projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();
		int rowsUpdated = 0;

		switch (uriType) {
		case TASKS:
			rowsUpdated = sqlDB.update(TASKS_TABLE_NAME, values, selection, selectionArgs);
			break;
		case TASKS_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(TASKS_TABLE_NAME, values, TASKS_TABLE_COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(TASKS_TABLE_NAME, values, TASKS_TABLE_COLUMN_ID + "=" + id
						+ " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
