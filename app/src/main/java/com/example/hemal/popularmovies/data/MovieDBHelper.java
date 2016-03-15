package com.example.hemal.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.hemal.popularmovies.data.MovieContract.MovieEntry;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hemal on 15/3/16.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    //In future, increment the count if changing the database
    private static final int DATABASE_VERSION = 1;

    //name of the database
    private static final String DATABASE_NAME = "MovieDB.db";


    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * Create the table as follow
         *  CREATE TABLE TABLE_NAME(colName1, colName2, colName3,..,colNameN);
         */

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "("
                + MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY"
                + MovieEntry.COLUMN_TITLE  + " TEXT NOT NULL"
                + MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL"
                + MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL"
                + MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL"
                + MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL"
                + MovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL"
                + " );";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /**
         * Only executed if changing the database version.
         * I am just creating it as a safe side only!
         */

        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
