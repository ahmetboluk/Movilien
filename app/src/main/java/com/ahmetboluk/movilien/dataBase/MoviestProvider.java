package com.ahmetboluk.movilien.dataBase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;


public class MoviestProvider extends ContentProvider {
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int URICODE_CATEGORIES = 1;
    private static final int URICODE_MOVIESTITEM = 2;
    static {
        matcher.addURI(MoviestContract.CONTENT_AUTHORITY, MoviestContract.PATH_CATEGORIES, URICODE_CATEGORIES);
        matcher.addURI(MoviestContract.CONTENT_AUTHORITY, MoviestContract.PATH_MOVIESTITEM, URICODE_MOVIESTITEM);
    }
    private SQLiteDatabase db;
    private DatabaseHelper helper;
    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        db = helper.getWritableDatabase();
        return true;
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (matcher.match(uri)) {
            case URICODE_CATEGORIES:
                cursor = db.query(MoviestContract.CategoriesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            case URICODE_MOVIESTITEM:
                cursor = db.query(MoviestContract.MoviestItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("BILINMEYEN QUERY URI" + uri);
        }
        return cursor;
    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case URICODE_CATEGORIES:
                return myInsert(uri, values, MoviestContract.CategoriesEntry.TABLE_NAME);
            case URICODE_MOVIESTITEM:
                return myInsert(uri, values, MoviestContract.MoviestItemEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("INSERT BILINMEYEN URI:" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int i;
        switch (matcher.match(uri)) {
            case URICODE_CATEGORIES:
                i = db.delete(MoviestContract.CategoriesEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case URICODE_MOVIESTITEM:
                i = db.delete(MoviestContract.MoviestItemEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("BILINMEYEN QUERY URI" + uri);
        }
        return i;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (matcher.match(uri)){
            case URICODE_CATEGORIES:
                //nada
                break;
            case URICODE_MOVIESTITEM:
                count = db.update(MoviestContract.MoviestItemEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return count;
    }

    private Uri myInsert(Uri uri, ContentValues values, String tableName) {
        long id = db.insert(tableName, null, values);
        if (id == -1) {
            Log.e("NotDefterimProvider", "INSERT HATA" + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }
}

