package com.example.hemal.popularmovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hemal on 14/2/16.
 */
public class DetailActivityFragment extends Fragment{

    @Bind(R.id.rl_detail_activity) RelativeLayout main_container;
    @Bind(R.id.main_poster_detail_activity) ImageView bigPoster;
    @Bind(R.id.small_poster_detail_activity) ImageView smallPoster;
    @Bind(R.id.tv_title_detail_page) TextView title;   //, description, release_date, user_rating;
    @Bind(R.id.description_detail) TextView description;
    @Bind(R.id.user_rating_description) TextView user_rating;
    @Bind(R.id.release_date_description) TextView release_date;

    @Bind(R.id.fab_detail_activity) FloatingActionButton fab;
    @Bind(R.id.fab_favourite) FloatingActionButton favourite;
    @Bind(R.id.fab_reviews) FloatingActionButton reviews;
    @Bind(R.id.fab_trailers) FloatingActionButton trailers;

    MovieParcelable movieParcelable = null;
    ArrayList<String> trailerLinks = null;

    private boolean menuShown = false;

    LinkGenerator linkGenerator;

    Resources resources;

    private static final String TAG = DetailActivityFragment.class.getSimpleName();
    public DetailActivityFragment(){
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
        * Getting a reference to the parcelable object passed on..
        * */
        movieParcelable = (MovieParcelable) getArguments().get(getActivity()
                .getResources()
                .getString(R.string.single_movie));
        resources = getActivity().getResources();


        View v = inflater.inflate(R.layout.detail_activity, container, false);


        linkGenerator = new LinkGenerator(getActivity());
        trailerLinks = linkGenerator.getTrailerLinks(movieParcelable.id);
        ButterKnife.bind(this, v);

        Picasso.with(getActivity()).load(movieParcelable.backdrop_path).into(bigPoster);
        Picasso.with(getActivity()).load(movieParcelable.poster_path).into(smallPoster);

        title.setText(formatString(movieParcelable.title));
        description.setText(movieParcelable.overview);
        release_date.setText(movieParcelable.release_date);
        String user_rating_string = String.valueOf(movieParcelable.vote_average)
                + Html.fromHtml(getActivity().getString(R.string.star_symbol_unicode));
        user_rating.setText(user_rating_string);

        return v;
    }

    private String formatString(String title) {
        /**
        * In landscape mode the title of the movie spoils the look.
        * So here i am formatting the title so that after every two words, there is a newline character.
        * */

        int wordCount = 0;
        StringBuilder sb = new StringBuilder(title);
        for (int i = 0; i< sb.length(); i++){
            if(sb.charAt(i) == ' ' ){
                wordCount++;
                if(wordCount % 3 == 0){
                    sb.replace(i, i+1, "\n");
                }
            }
        }
        return sb.toString();
    }

    @OnClick(R.id.fab_detail_activity)
    public void toggleFloatingSubMenu() {

        Animation animationShow = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        Animation animationHide = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        if(menuShown){

            favourite.startAnimation(animationHide);
            favourite.setClickable(false);

            reviews.startAnimation(animationHide);
            reviews.setClickable(false);

            trailers.startAnimation(animationHide);
            trailers.setClickable(false);

            menuShown = !menuShown;
        } else {

            favourite.startAnimation(animationShow);
            favourite.setClickable(true);

            reviews.startAnimation(animationShow);
            reviews.setClickable(true);

            trailers.startAnimation(animationShow);
            trailers.setClickable(true);

            menuShown = !menuShown;
        }

    }


    @OnClick(R.id.fab_reviews)
    public void showReviews(){
        if(menuShown)
            toggleFloatingSubMenu();

        Intent intent = new Intent(getActivity(), ReviewClass.class);
        intent.putExtra(getActivity().getString(R.string.movie_id), movieParcelable.id);
        intent.putExtra(getActivity().getString(R.string.movie_name), movieParcelable.title);
        startActivity(intent);
    }

    @OnClick(R.id.fab_trailers)
    public void showTrailers(){
        if(menuShown)
            toggleFloatingSubMenu();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Watch Trailers")
                .setCancelable(true)
                .setItems(trailerLinks.toArray(new CharSequence[trailerLinks.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openLinkIntent(trailerLinks.get(which));
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openLinkIntent(String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        startActivity(intent);
    }

}










