package com.example.android.spiderintask3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.Toast;

/**
 * Created by Srikanth on 7/8/2016.
 */
public class MovieDbUtilities implements BaseColumns {

    //Column names which are defined in the favourites table
    //present in the local movie database.
    public static final String TABLE_NAME = "favourites";
    public static final String COLUMN_IMDB_ID = "imdb_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_PLOT = "plot";
    public static final String COLUMN_TYPE = "type";


    public long insert(Context context, String tableName, ContentValues values){
        MovieDbHelper mOpenHelper = new MovieDbHelper(context);
        String movieId = values.getAsString("imdb_id");
        long insertedRowId=-2;

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor c = query(context, tableName, new String[]{MovieDbUtilities.COLUMN_IMDB_ID}, MovieDbUtilities.COLUMN_IMDB_ID+"=?",
                new String[]{movieId}, null);

        if(c.getCount()==0) {
            insertedRowId = db.insert(tableName, null, values);
        }else{
            insertedRowId = -3;
        }

        if(insertedRowId==-1)
            Toast.makeText(context, "Failed to insert into database", Toast.LENGTH_SHORT).show();

        db.close();
        return insertedRowId;
    }

    public Cursor query(Context context, String tableName, String[] projection, String selection, String[] selectionArgs,
                         String sortOrder){
        Cursor retCursor;
        SQLiteDatabase db = new MovieDbHelper(context).getReadableDatabase();

        retCursor = db.query(
                tableName,
                projection,     //Column names required
                selection,      //Rows required with
                selectionArgs,  //Criteria for row selection
                null,
                null,
                sortOrder
        );
        return retCursor;
    }
}
