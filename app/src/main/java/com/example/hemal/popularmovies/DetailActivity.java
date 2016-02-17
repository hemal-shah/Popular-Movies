package com.example.hemal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hemal on 14/2/16.
 */
public class DetailActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "DETAIL_ACTIVITY_FRAGMENT";
    int layout_into = R.id.frame_layout_detail_activity;
    private static final String TAG = DetailActivity.class.getSimpleName();
    DetailActivityFragment fragment = new DetailActivityFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_fragment);


        /**
         * getting the data passed to this activity,
         * then forwarding it ahead to the fragment through setArguments(Bundle args) method.
         * */
        Intent incomingIntent = getIntent();
        MovieParcelable movieParcelable =  incomingIntent.getParcelableExtra(
                getResources().getString(R.string.parcel)
        );

        Bundle arguments = new Bundle();
        arguments.putParcelable(getResources().getString(R.string.single_movie),
                movieParcelable);

        fragment.setArguments(arguments);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(layout_into, fragment, FRAGMENT_TAG)
                    .commit();
        }
    }
}
