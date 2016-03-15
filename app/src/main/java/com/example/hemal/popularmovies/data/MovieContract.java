package com.example.hemal.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hemal on 15/3/16.
 */
public class MovieContract {

    //The content authority for content providers!
    public static final String CONTENT_AUTHORITY = "com.example.hemal.popularmovies";

    //Creating the base of all URI's
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //We want to add the path as content://com.example.hemal.popularmovies/movies.
    public static final String PATH_MOVIES = "movies";


    public static final class MovieEntry implements BaseColumns{

        //Creating the uri for accessing the data.
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES).build();


        //content type and content item type
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        //table name
        public static final String TABLE_NAME = "movie_table";

        //creating instances for all the required columns.
        //this are similiar to the items mentioned in MovieParcelable class.
        public static final String COLUMN_MOVIE_ID = "id"; //int
        public static final String COLUMN_TITLE = "title"; //String
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path"; //string
        public static final String COLUMN_VOTE_AVG = "vote_average"; //float
        public static final String COLUMN_OVERVIEW = "overview"; //long string
        public static final String COLUMN_POSTER_PATH = "poster_path"; //string
        public static final String COLUMN_RELEASE_DATE = "release_date"; //date or string
        public static final String COLUMN_POPULARITY = "popularity"; //double


        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
