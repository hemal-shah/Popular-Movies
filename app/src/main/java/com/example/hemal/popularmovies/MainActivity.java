package com.example.hemal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private static final String MAIN_ACTIVITY_FRAGMENT_TAG = "MainActivityFragment";
    private static final String DETAIL_ACTIVITY_FRAGMENT_TAG = "DetailActivityFragment";

    DetailActivityFragment fragment;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Bind(R.id.toolbar_activity_main)
    Toolbar toolbar;

    boolean mTwoPane;

    /*
    * This is our main page for the application.
    * Creating a fragment and then inflating it into the frame layout
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (findViewById(R.id.frame_layout_detail_activity) != null) {
            //Indicates that two pane layout is enabled.
            mTwoPane = true;
            if (savedInstanceState == null) {
                loadMovie(null);
            }
        } else {
            //Indicates that two pane layout is absent.
            mTwoPane = false;
        }


    }

    @Override
    public void showDetails(MovieParcelable movieParcelable) {
        if (!mTwoPane) {
            Intent newActivity = new Intent(this, DetailActivity.class);
            newActivity.putExtra(getResources().getString(R.string.parcel), movieParcelable);
            startActivity(newActivity);
        } else {
            loadMovie(movieParcelable);
        }
    }

    @Override
    public void showFirstItem(MovieParcelable movieParcelable) {
        if (mTwoPane)
            loadMovie(movieParcelable);
    }

    public void loadMovie(@Nullable MovieParcelable movie) {

        if (movie != null) {

            fragment = new DetailActivityFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable(getResources().getString(R.string.single_movie),
                    movie);

            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_detail_activity, fragment, DETAIL_ACTIVITY_FRAGMENT_TAG)
                    .commit();
        }
    }
}
