package com.example.hemal.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 *Fragment containing recycler view to show a grid list of movies..
 */


public class MainActivityFragment extends Fragment implements HomePageAdapter.Callback, LinkGenerator.DataUpdate {


    @Bind(R.id.rv_content_main) RecyclerView recyclerView;
    Resources resources ;
    HomePageAdapter adapter;

    private static final String INSTANCE_KEY = "movie_data";
    ArrayList<MovieParcelable> movieParcelables;
    LinkGenerator linkGenerator;

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = getActivity().getResources();
        linkGenerator = new LinkGenerator(getActivity(), this);

        /*
         * Retrieve data if the user has already downloaded the content..from saveInstanceState
         * */


        if (savedInstanceState == null || !savedInstanceState.containsKey(INSTANCE_KEY)) {
            movieParcelables = linkGenerator.preferenceMode(resources.getString(R.string.popularity_sort));
        } else {
            movieParcelables = savedInstanceState.getParcelableArrayList(INSTANCE_KEY);
            if(adapter != null){
                adapter.notifyDataSetChanged();
            }
        }

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

        ButterKnife.bind(this, v);

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
                movieParcelables  = linkGenerator.preferenceMode(resources.getString(R.string.rating_sort));
                adapter.notifyDataSetChanged();
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                return true;
            case R.id.menu_popularity:
                movieParcelables = linkGenerator.preferenceMode(resources.getString(R.string.popularity_sort));
                adapter.notifyDataSetChanged();
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * @param movieParcelable Whenever a movie poster is clicked, this method will be helpful to notify which item was clicked
     *                        based on the Parcelable object, we can use that particular object and then redirect it.
     */
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

    /**
     * When the url is passed to the LinkGenerator class, it returns before the data is retrieved.
     * So, when the data is completely retrieved, this method is called, so that the adapter can be notified
     * and the contents can be shown in the UI.
     */
    @Override
    public void onDataLoaded() {
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * There may be different cases when the data cannot be loaded. So, if data is not loaded, this method is used to
     * show the error messages.
     */
    @Override
    public void onDataLoadFail() {
        if(recyclerView != null){
            Snackbar.make(recyclerView, resources.getString(R.string.data_fail), Snackbar.LENGTH_SHORT).show();
        }
    }
}