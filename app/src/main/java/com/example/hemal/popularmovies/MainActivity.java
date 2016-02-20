package com.example.hemal.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_FRAGMENT_TAG = "MainActivityFragment";

    @Bind(R.id.toolbar_activity_main) Toolbar toolbar ;

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


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_activity_main, new MainActivityFragment(), MAIN_ACTIVITY_FRAGMENT_TAG)
                    .commit();
        }
    }
}
