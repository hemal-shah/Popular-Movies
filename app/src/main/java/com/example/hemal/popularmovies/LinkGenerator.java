package com.example.hemal.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.http.Url;

/**
 * Created by hemal on 20/2/16.
 */
public class LinkGenerator {

    private static final String TAG = LinkGenerator.class.getSimpleName();

    ArrayList<MovieParcelable> movieParcelables;
    ArrayList<ReviewParcelable> reviewParcelables;
    Context mContext;


    //for the json retrieval temp objects..
    JSONArray jsonArray;
    JSONObject singleObjectResult;
    String poster_path, overview, release_date, title, backdrop_path;
    int id;
    URL url = null;
    double popularity;
    float vote_average;
    //end of objects required in parsing json

    DataUpdate mDataUpdate;


    String BASE_URL = "http://api.themoviedb.org/3/discover/movie"; //base url to retrieve json data..

    /*Defining all the element names used in the key value pairs in the json returned from themoviedb*/
    private static final String RESULTS = "results";
    private static final String POSTER_PATHS = "poster_path";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String IMAGE_BASE_URL_BIG = "http://image.tmdb.org/t/p/w500";
    private static final String RELEASE_DATE = "release_date";
    private static final String OVERVIEW = "overview";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String POPULARITY = "popularity";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String MOVIE_BASE_LINK = "http://api.themoviedb.org/3/movie/";
    private static final String SITE = "site";
    private static final String KEY = "key";
    private static final String YOUTUBE = "YouTube";

    /*Done defining constant strings*/


    public LinkGenerator(Context mContext, MainActivityFragment object) {
        movieParcelables = new ArrayList<>();
        this.mContext = mContext;
        this.mDataUpdate = object;
    }


    public LinkGenerator(Context mContext) {
        this.mContext = mContext;
    }


    public LinkGenerator(Context mContext, ReviewClass object) {
        this.mContext = mContext;
        this.mDataUpdate = object;
    }

    /**
     * @param preference Specifies in what order you want to generate the list of movies. Options are currently either
     *                   based on popularity or rating.
     * @return Returns a list of movies, in a form of arraylist so that it can be displayed in an adapter.
     */
    public ArrayList<MovieParcelable> preferenceMode(final String preference) {
        /**
         * Method created to retrieve data from the moviedb.org website
         * Based on the string "preference" tha json data will be retrieved and then displayed!
         * */

        movieParcelables.clear();

        //Creating the uri..
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("sort_by", preference + ".desc")
                .appendQueryParameter("api_key", mContext.getString(R.string.api_key))
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Exception Occured : " + e.toString());
        }


        /*
         * JsonObjectRequest is a method defined in the volley library to retrieve json object from a url.
         * If in need to retrieve an json array use JsonArrayRequest
         */


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, //method
                url.toString(), //url
                null, //data to send if any..in json format
                new Response.Listener<JSONObject>() { //listener
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray(RESULTS);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                singleObjectResult = jsonArray.getJSONObject(i);
                                poster_path = IMAGE_BASE_URL + singleObjectResult.getString(POSTER_PATHS);
                                overview = singleObjectResult.getString(OVERVIEW);
                                release_date = singleObjectResult.getString(RELEASE_DATE);
                                id = singleObjectResult.getInt(ID);
                                title = singleObjectResult.getString(TITLE);
                                backdrop_path = IMAGE_BASE_URL_BIG + singleObjectResult.getString(BACKDROP_PATH);
                                popularity = singleObjectResult.getDouble(POPULARITY);
                                vote_average = (float) singleObjectResult.getDouble(VOTE_AVERAGE);

                                movieParcelables.add(new MovieParcelable(title, release_date, poster_path, overview, backdrop_path, id, vote_average, popularity));
                            }

                            mDataUpdate.onDataLoaded();
                        } catch (JSONException e) {
                            Log.i(TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() { //action to be performed on error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDataUpdate.onDataLoadFail();
                    }
                }
        );

        ApplicationClass.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
        return movieParcelables;
    }


    /**
     * Interface created to update the calling class when the data is completely loaded so that they can display it,
     * or if the dataload failed, so that they can display any error messages.
     * <p/>
     * <p/>
     * Exception management not possible currently. TODO : add exception management.
     */
    public interface DataUpdate {
        void onDataLoaded();

        void onDataLoadFail();
    }

    public interface SingleMovieDataUpdate {
        void onTrailersLoaded();
    }

    private static final String YOUTUBE_BASE_LINK = "https://www.youtube.com/watch?v=";

    public ArrayList<String> getTrailerLinks(int id) {
        Uri uri = Uri.parse(MOVIE_BASE_LINK).buildUpon()
                .appendEncodedPath(String.valueOf(id))
                .appendEncodedPath("videos")
                .appendQueryParameter("api_key", mContext.getString(R.string.api_key))
                .build();

        final ArrayList<String> responseList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = null;
        try {
            URL url = new URL(uri.toString());

            jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(RESULTS);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject tempObject = jsonArray.getJSONObject(i);
                                    String site = tempObject.getString(SITE);
                                    if (site.equalsIgnoreCase(YOUTUBE)) {
                                        String key = tempObject.getString(KEY);
                                        responseList.add(YOUTUBE_BASE_LINK + key);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ApplicationClass.getInstance().addToRequestQueue(jsonObjectRequest);
        return responseList;
    }


    public ArrayList<ReviewParcelable> getReviewParcelables(int id) {
        Uri uri = Uri.parse(MOVIE_BASE_LINK).buildUpon()
                .appendEncodedPath(String.valueOf(id))
                .appendEncodedPath("reviews")
                .appendQueryParameter("api_key", mContext.getResources().getString(R.string.api_key))
                .build();


        reviewParcelables = new ArrayList<>();
        try {
            URL url = new URL(uri.toString());
            Log.i(TAG, url.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i(TAG, "The response is : " + response.toString());
                                JSONArray jsonArray = response.getJSONArray(RESULTS);
                                JSONObject jsonObject;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String author = jsonObject.getString("author");
                                    String review = jsonObject.getString("content");
                                    reviewParcelables.add(new ReviewParcelable(review, author));
                                }
                                mDataUpdate.onDataLoaded();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mDataUpdate.onDataLoadFail();
                        }
                    }
            );


            ApplicationClass.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return reviewParcelables;
    }

}