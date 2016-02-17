package com.example.hemal.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by hemal on 11/2/16.
 */
public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder>{

    /*
    * This adapter is to view a list of images of the movies to user on the main page.
    * */

    ArrayList<MovieParcelable> arrayList;
    Context context;
    int layout_resource;
    Callback callback;
    private static final String TAG = HomePageAdapter.class.getSimpleName();

    HomePageAdapter(Context context, int layout_resource, ArrayList<MovieParcelable> arrayList, MainActivityFragment object){

        this.context = context;
        this.layout_resource = layout_resource;
        this.arrayList = arrayList;
        this.callback = object;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /*
        * Creates the view based on requirements.
        * */

        View v = LayoutInflater.from(this.context).inflate(layout_resource, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final MovieParcelable movieParcelable = arrayList.get(position);

        Picasso.with(this.context).load(movieParcelable.poster_path).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * Whenever user clicks on any one of the imageview pass the movieParcelable object related to that image
                * back to the Activity/Fragment containing that recyclerView
                * */

                if(callback != null){
                    callback.itemClicked(movieParcelable);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        /*
        * If the arrayList would be null, then it may be possible
        * that some network error has occurred or null arraylist is passed
        * that may cause the app to crash
        * so if the arraylist is empty or is null, return 0 else return the size.
        * */

        return (arrayList.size() == 0 || arrayList == null)? 0: arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        /*
        * Simple viewHolder for the main page of the screen.
        * */
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView_single_movie_poster_image);
        }

    }

    public interface Callback{
        /*
        * Simple touch interface created to handle user touch events.
        * As the main purpose of "ADAPTERS" is to handle view creation according to requirement
        * they should not be handling the user click events.
        * */

        void itemClicked(MovieParcelable parcelable);
    }
}
