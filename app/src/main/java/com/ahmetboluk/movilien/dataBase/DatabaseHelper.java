package com.ahmetboluk.movilien.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmetboluk.movilien.dataBase.MoviestContract.CategoriesEntry;
import com.ahmetboluk.movilien.dataBase.MoviestContract.MoviestItemEntry;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movilien.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CATEGOIES_CREATE =
            "CREATE TABLE " + CategoriesEntry.TABLE_NAME + " (" +
                    CategoriesEntry._ID + " INTEGER PRIMARY KEY, " +
                    CategoriesEntry.COLUMN_CATEGORY + " TEXT)";

    public static final String TABLE_MOVIESTITEM_CREATE =
            "CREATE TABLE " + MoviestItemEntry.TABLE_NAME + " (" +
                    MoviestItemEntry._ID + " INTEGER PRIMARY KEY, " +
                    MoviestItemEntry.COLUMN_ID + " TEXT, " +
                    MoviestItemEntry.COLUMN_NAME + " TEXT, " +
                    MoviestItemEntry.COLUMN_RATE + " TEXT, " +
                    MoviestItemEntry.COLUMN_THUMBNAIL_URL + " TEXT, " +
                    MoviestItemEntry.COLUMN_TYPE + " TEXT, " +
                    MoviestItemEntry.COLUMN_YEAR + " TEXT, " +
                    MoviestItemEntry.COLUMN_CATEGORY + " INTEGER," +
                    " FOREIGN KEY (" + MoviestItemEntry.COLUMN_CATEGORY + ") REFERENCES " +
                    CategoriesEntry.TABLE_NAME + " (" + CategoriesEntry._ID + ") )";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //TABLO İLK DEFA OLUŞTURULDUĞUNDA ÇALIŞAN METOD
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CATEGOIES_CREATE);
        sqLiteDatabase.execSQL(TABLE_MOVIESTITEM_CREATE);
    }

    //DATABASE ÜZERİNDE TABLO EKLEM ÇIKARMA VS YAPILACAĞIZAMAN KULLANILACAK METOD DUR.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE İF EXISTS " +CategoriesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE İF EXISTS " +MoviestItemEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }


}
