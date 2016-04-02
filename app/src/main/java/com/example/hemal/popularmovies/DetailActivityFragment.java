package com.example.hemal.popularmovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
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
public class DetailActivityFragment extends Fragment {

    @Bind(R.id.rl_detail_activity)
    RelativeLayout main_container;
    @Bind(R.id.main_poster_detail_activity)
    ImageView bigPoster;
    @Bind(R.id.small_poster_detail_activity)
    ImageView smallPoster;
    @Bind(R.id.tv_title_detail_page)
    TextView title;   //, description, release_date, user_rating;
    @Bind(R.id.description_detail)
    TextView description;
    @Bind(R.id.user_rating_description)
    TextView user_rating;
    @Bind(R.id.fab_detail_activity)
    FloatingActionButton fab;
    @Bind(R.id.release_date_description)
    TextView release_date;
    @Bind(R.id.fab_favourite)
    FloatingActionButton favourite;
    @Bind(R.id.fab_reviews)
    FloatingActionButton reviews;
    @Bind(R.id.fab_trailers)
    FloatingActionButton trailers;
    @Bind(R.id.fab_share)
    FloatingActionButton share;

    MovieParcelable movieParcelable = null;
    ArrayList<String> trailerLinks = null;

    private boolean menuShown = false;
    private boolean isAlreadyFavourite = false; //Indicates whether the movie is already marked favourite.

    DataControl dataControl;

    Resources resources;


    private static final String TAG = DetailActivityFragment.class.getSimpleName();

    public DetailActivityFragment() {
    }

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


        dataControl = new DataControl(getActivity());
        trailerLinks = dataControl.getTrailerLinks(movieParcelable.id);
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
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == ' ') {
                wordCount++;
                if (wordCount % 3 == 0) {
                    sb.replace(i, i + 1, "\n");
                }
            }
        }
        return sb.toString();
    }

    @OnClick(R.id.fab_detail_activity)
    public void toggleFloatingSubMenu() {

        Animation animationShow = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        Animation animationHide = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        OvershootInterpolator interpolator = new OvershootInterpolator();

        if (menuShown) {

            ViewCompat.animate(fab).rotation(0f).withLayer().setDuration(100).setInterpolator(interpolator).start();

            favourite.startAnimation(animationHide);
            favourite.setClickable(false);

            reviews.startAnimation(animationHide);
            reviews.setClickable(false);

            trailers.startAnimation(animationHide);
            trailers.setClickable(false);

            share.startAnimation(animationHide);
            share.setClickable(false);

            menuShown = !menuShown;
        } else {
            ViewCompat.animate(fab).rotation(45f).withLayer().setDuration(100).setInterpolator(interpolator).start();
            favourite.startAnimation(animationShow);
            favourite.setClickable(true);

            reviews.startAnimation(animationShow);
            reviews.setClickable(true);

            trailers.startAnimation(animationShow);
            trailers.setClickable(true);

            share.startAnimation(animationShow);
            share.setClickable(true);


            menuShown = !menuShown;
        }

    }


    @OnClick(R.id.fab_reviews)
    public void showReviews() {
        if (menuShown)
            toggleFloatingSubMenu();

        Intent intent = new Intent(getActivity(), ReviewClass.class);
        intent.putExtra(getActivity().getString(R.string.movie_id), movieParcelable.id);
        intent.putExtra(getActivity().getString(R.string.movie_name), movieParcelable.title);
        startActivity(intent);
    }

    @OnClick(R.id.fab_trailers)
    public void showTrailers() {
        CharSequence[] charSequences = new CharSequence[trailerLinks.size()];
        for (int i = 0; i < trailerLinks.size(); i++)
            charSequences[i] = "Trailer " + (i + 1);

        if (menuShown)
            toggleFloatingSubMenu();

        if (trailerLinks != null && trailerLinks.size() != 0) {

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Watch Trailers")
                .setCancelable(true)
                .setItems(charSequences, new DialogInterface.OnClickListener() {
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


    @OnClick(R.id.fab_favourite)
    public void toggleFavourite() {

        /**
         * First check whether the movie is already marked as favourite or not
         * If marked as favourite => Ask user if he/she wants to remove it from favourites.
         * If not, simple add it to the database.
         */


        isAlreadyFavourite = dataControl.queryForExistingMovie(movieParcelable.id);

        if (menuShown)
            toggleFloatingSubMenu();

        if (isAlreadyFavourite) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(movieParcelable.title)
                    .setMessage("This movie is already marked as favourite. Do you want to remove it from favourites?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar.make(main_container, R.string.removing_from_favourites, Snackbar.LENGTH_SHORT).show();
                            boolean isDeleted = dataControl.removeFromFavourites(movieParcelable.id);
                            if (isDeleted) {
                                Snackbar.make(main_container, R.string.removed_from_favourites, Snackbar.LENGTH_SHORT).show();
                                isAlreadyFavourite = false;
                            } else {
                                Snackbar.make(main_container, R.string.cant_remove_from_favourites, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Snackbar.make(getView(), R.string.marking_as_favourite, Snackbar.LENGTH_SHORT).show();
            boolean isInserted = dataControl.insertMovieToDB(movieParcelable);
            if (isInserted) {
                Snackbar.make(main_container, R.string.marked_as_favourite, Snackbar.LENGTH_SHORT).show();
                isAlreadyFavourite = true;
            } else {
                Snackbar.make(main_container, R.string.some_error_occured, Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    @OnClick(R.id.fab_share)
    public void shareTrailer() {

        if (menuShown)
            toggleFloatingSubMenu();

        if (trailerLinks != null || trailerLinks.size() != 0) {

            StringBuilder sb = new StringBuilder();
            sb.append("Hey!").append("\n")
                    .append("Check out the trailer for ")
                    .append(movieParcelable.title)
                    .append(" at ")
                    .append(trailerLinks.get(0))
                    .append("\n")
                    .append("#PopularMovies");


            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, sb.toString());
            startActivity(Intent.createChooser(intent, "Share using..."));
        } else {
            Snackbar.make(main_container, R.string.no_trailer_available, Snackbar.LENGTH_SHORT).show();
        }
    }
}