package com.ahmetboluk.movilien.dataBase;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviestContract {

    public static final String CONTENT_AUTHORITY = "com.ahmetboluk.movilien.dataBase.MoviestProvider";
    public static final String PATH_CATEGORIES = "categories";
    public static final String PATH_MOVIESTITEM = "moviestItem";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class CategoriesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORIES);
        public static final String TABLE_NAME = "categories";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CATEGORY = "category";
    }

    public static final class MoviestItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIESTITEM);
        public static final String TABLE_NAME = "moviestItem";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ID = "itemid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailUrl";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_CATEGORY = "categoryId";
    }
}
