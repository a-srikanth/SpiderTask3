package com.example.android.spiderintask3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Srikanth on 7/8/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movies.db";
    Context mContext;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " + MovieDbUtilities.TABLE_NAME+" ("+
                MovieDbUtilities._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieDbUtilities.COLUMN_IMDB_ID + " TEXT NOT NULL, "+
                MovieDbUtilities.COLUMN_TITLE + " TEXT NOT NULL, "+
                MovieDbUtilities.COLUMN_YEAR + " TEXT NOT NULL, "+
                MovieDbUtilities.COLUMN_GENRE + " TEXT NOT NULL, "+
                MovieDbUtilities.COLUMN_RATING + " TEXT NOT NULL, "+
                MovieDbUtilities.COLUMN_POSTER_PATH + " TEXT NOT NULL, "+
                MovieDbUtilities.COLUMN_PLOT + " TEXT NOT NULL, "+
                MovieDbUtilities.COLUMN_TYPE + " TEXT NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olderVersion, int newerVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDbUtilities.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
