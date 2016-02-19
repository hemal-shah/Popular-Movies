package com.example.hemal.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Collections;
import java.util.Comparator;

/*
 *Fragment containing recycler view to show a grid list of movies..
 */
public class MainActivityFragment extends Fragment implements HomePageAdapter.Callback {


    RecyclerView recyclerView;
    Resources resources ;
    HomePageAdapter adapter;
    URL url = null;
    String BASE_URL = "http://api.themoviedb.org/3/discover/movie"; //base url to retrieve json data..
    ArrayList<MovieParcelable> movieParcelables;


    //for the json retrieval temp objects..
    JSONArray jsonArray;
    JSONObject singleObjectResult;
    String poster_path, overview, release_date, title, backdrop_path;
    int id;
    double popularity;
    float vote_average;
    //end of objects required in parsing json


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
    private static final String INSTANCE_KEY = "movie_data";
    /*Done defining constant strings*/

    private static final String TAG = MainActivityFragment.class.getSimpleName();


    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = getActivity().getResources();

        /*
         * Retrieve data if the user has already downloaded the content..from saveInstanceState
         * */


        if (savedInstanceState == null || !savedInstanceState.containsKey(INSTANCE_KEY)) {
            movieParcelables = new ArrayList<>();
            preferenceMode(resources.getString(R.string.popularity_sort));
        } else {
            movieParcelables = savedInstanceState.getParcelableArrayList(INSTANCE_KEY);
            Log.i(TAG, "Data already stored...retrieved it.." + movieParcelables.toString());
        }

    }


    public void preferenceMode(final String preference){
        /**
         * Method created to retrieve data from the moviedb.org website
         * Based on the string "preference" tha json data will be retrieved and then displayed!
         * */


        movieParcelables.clear();

        //Creating the uri..
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("sort_by", preference + ".desc")
                .appendQueryParameter("api_key", getActivity().getString(R.string.api_key))
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

                            if(adapter != null){
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Log.i(TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() { //action to be performed on error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(recyclerView != null){
                            Snackbar.make(recyclerView, resources.getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        ApplicationClass.getInstance().addToRequestQueue(jsonObjectRequest, TAG);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /*
        * Saving the user's data.
        * */

        outState.putParcelableArrayList(INSTANCE_KEY, movieParcelables);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.content_main, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_content_main);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }
        adapter = new HomePageAdapter(getActivity(), R.layout.single_movie_poster_image, movieParcelables, MainActivityFragment.this);
        recyclerView.setAdapter(adapter);

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_sort_order, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
        * based on the user preference
        * call the required method to sort, sortOrder(0 (or) 1)
        * and also update the radio box in the menu to retain changes.
        * */

        int id = item.getItemId();
        switch (id) {
            case R.id.menu_highest_rating:
                preferenceMode(resources.getString(R.string.rating_sort));
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                return true;
            case R.id.menu_popularity:
                preferenceMode(resources.getString(R.string.popularity_sort));
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(MovieParcelable movieParcelable) {
        /*
        * This is implement due to the interface HomePageAdapter.Callback..
        * This is called whenever the user clicks on one of the images..
        * We need to pass that parcelable to the detail activity so that it can be used to retrieve data.
        * */

        Intent newActivity = new Intent(getActivity(), DetailActivity.class);
        newActivity.putExtra(getActivity().getResources().getString(R.string.parcel), movieParcelable);
        startActivity(newActivity);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        /*
        * In landscape mode provide 4 columns to the layout manager, and in portrait mode provide 2 columns
        * */

        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }
}
