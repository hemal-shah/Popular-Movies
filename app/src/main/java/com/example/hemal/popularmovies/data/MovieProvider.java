package com.example.hemal.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by hemal on 15/3/16.
 */
public class MovieProvider extends ContentProvider {

    /**
     * this content provider acts as an interface between the database
     * and the UI of the application.
     */

    static final int MOVIE = 100; //id for the UriMatcher.

    private MovieDBHelper movieDBHelper; //instance variable of the db class.

    private static SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
    static {
        sqLiteQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
    }

    private static final UriMatcher uriMatcher = buildUriMatcher();

    /**
     * Created to match the URIs coming to this content provider
     * and if it's a valid URI then generate int codes for accessing the data
     * from database, or else throw exception.
     *
     * @return UriMatcher for the movie's content provider.
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIE);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext()); //instantiate.
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor = null;
        /**
         * Defines what cursor to return based on the URI
         */
        switch (uriMatcher.match(uri)){
            case MOVIE:
                sqLiteQueryBuilder.query(
                        movieDBHelper.getReadableDatabase(), //database to query
                        projection, //which cols to retrieve
                        selection, //the where clause
                        selectionArgs, //columns to group by
                        null, //columns to filter by row groups
                        null,//sorting order...either asc or desc by column name
                        null //number of queries to return
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        //set notification on the parent uri, so whenever a change like insert,
        //update, delete, or refresh is performed, the UI is updated!
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        /**
         * Simple overriden  method which returns the return type of the
         * URI.
         * It can be either DIR for list of items,
         * or ITEM for a single row of item.
         */

        final int match = uriMatcher.match(uri);
        switch (match){
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        //first get database object to write.
        final SQLiteDatabase database = movieDBHelper.getWritableDatabase();

        //generate the match id.
        final int match = uriMatcher.match(uri);

        Uri returnURI;

        switch (match){
            case MOVIE:
                long _id = database.insert(MovieContract.MovieEntry.TABLE_NAME, //table into which entry has to be made.
                        null,//(don't know what nullColumnHack is.?)
                        values);
                if(_id > 0){
                    returnURI = MovieContract.MovieEntry.buildMovieUri(_id);
                    /**
                     * The above will generate a URI of something of the form
                     * content://com.example.hemal.popularmovies/movies/#####
                     * where #### is identifier relating to the entry in the datbase.
                     */
                } else{
                    throw new UnsupportedOperationException("Failed to insert row into : " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        //Notify all the hearing contents that the data has changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnURI;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        if(null == selection)
            selection = "1"; //delete all the rows if null is passed as selection.

        switch (match){
            case MOVIE:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, //name of table to delete rows from
                        selection, //the where condition or clause; e.g. WHERE _id == ? AND title == ?;
                        selectionArgs //The arguments which are replaced in the where clause query. of the form :- new String[]{paramters, equal to number of question marks in whereclause};
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        if(rowsDeleted != 0){
            /**
             * If we have deleted some rows, either 1 or more
             * then all the UI elements' cursor should be notified about that.
             */

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        /**
         * this application currently doesn't require update functionality
         */

        return 0;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match){
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        movieDBHelper.close();
        super.shutdown();
    }
}
