package com.example.hemal.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.hemal.popularmovies.data.MovieContract.MovieEntry;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hemal on 15/3/16.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    /**
     * For this application, this class simply creates a database
     * and if new version of the schema is to be defined, then make changes to the
     * table creation accordingly.
     */


    //In future, increment the count if changing the database
    private static final int DATABASE_VERSION = 1;

    //name of the database, make the preference of not changing it.
    private static final String DATABASE_NAME = "MovieDB.db";


    public MovieDBHelper(Context context) {
        //constructor
        super(context, //context
                DATABASE_NAME, //name of the database
                null, //cursor factory (I don't know what cursor factory is)
                DATABASE_VERSION //the version of database.
        );

        /**
         * Here if the version of the database changes,
         * then it is notified by the class that it has been updated.
         * And thus, the onUpgrade method is called.
         */
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * Indicates what actions to be performed when the application is first installed.
         * Never called afterwards.
         *
         * Create the table as follow
         *  CREATE TABLE TABLE_NAME(colName1, colName2, colName3,..,colNameN);
         */

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "("
                + MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY,"
                + MovieEntry.COLUMN_TITLE  + " TEXT NOT NULL,"
                + MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL,"
                + MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL,"
                + MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL,"
                + MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL,"
                + MovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL,"
                + MovieEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL"
                + " );";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /**
         * When the super constructor is called, and if the database version changes, then
         * this method's execution will start.
         *
         * Remove all the existing tables, and then call the oncreate method, to create new
         * database.
         *
         * If we are storing some important information about the user, then
         * we should take care while altering the database / schema.
         *
         * In this application, I don't store user specific data,
         * just some movies retreived from the net
         * and storing them in the database.
         */

        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
