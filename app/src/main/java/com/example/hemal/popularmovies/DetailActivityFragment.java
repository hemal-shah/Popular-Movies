package com.example.hemal.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hemal on 14/2/16.
 */
public class DetailActivityFragment extends Fragment {

    @Bind(R.id.rl_detail_activity) RelativeLayout main_container;
    @Bind(R.id.main_poster_detail_activity) ImageView bigPoster;
    @Bind(R.id.small_poster_detail_activity) ImageView smallPoster;
    @Bind(R.id.tv_title_detail_page) TextView title;   //, description, release_date, user_rating;
    @Bind(R.id.description_detail) TextView description;
    @Bind(R.id.user_rating_description) TextView user_rating;
    @Bind(R.id.release_date_description) TextView release_date;


    private static final String TAG = DetailActivityFragment.class.getSimpleName();
    public DetailActivityFragment(){
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
        * Getting a reference to the parcelable object passed on..
        * */
        MovieParcelable movieParcelable = (MovieParcelable) getArguments().get(getActivity()
                .getResources()
                .getString(R.string.single_movie));


        View v = inflater.inflate(R.layout.detail_activity, container, false);

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
        /*
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

}
