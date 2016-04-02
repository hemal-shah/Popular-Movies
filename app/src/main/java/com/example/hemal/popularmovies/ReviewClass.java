package com.example.hemal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hemal on 13/3/16.
 */
public class ReviewClass extends AppCompatActivity implements DataControl.DataUpdate {


    @Bind(R.id.rv_review_page) RecyclerView recyclerView;
    ArrayList<ReviewParcelable> arrayList = null;
    DataControl dataControl;
    ReviewAdapter adapter;
    String movie_name;
    int movieID;
    @Bind(R.id.toolbar_review_page) Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_page);

        ButterKnife.bind(this);
        Intent incomingIntent = getIntent();
        movieID = incomingIntent.getIntExtra(getString(R.string.movie_id), -1);
        movie_name = incomingIntent.getStringExtra(getString(R.string.movie_name));
        setUpActivity();
        dataControl = new DataControl(this, this);
        arrayList = dataControl.getReviewParcelables(movieID);
        adapter = new ReviewAdapter(this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDataLoaded() {
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onDataLoadFail() {
        Snackbar.make(recyclerView, getString(R.string.data_fail), Snackbar.LENGTH_INDEFINITE).show();
    }


    public void setUpActivity(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(movie_name);
        }
    }
}
