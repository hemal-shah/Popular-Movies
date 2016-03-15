package com.example.hemal.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hemal on 12/2/16.
 */
public class MovieParcelable implements Parcelable {


    /*
    * Creating member variables for contents I want to store...and pass from the json retrieved from url
    * 1) poster_path (String)
    * 2) overview (String)
    * 3) release-date (String)
    * 4) id (int)
    * 5) title (String)
    * 6) backdrop_path (String)
    * 7) vote average (float)
    * 8) popularity (double)
    **/


    String title, release_date, poster_path, overview, backdrop_path;
    int id;
    float vote_average;
    double popularity;

    public MovieParcelable(String title, String release_date, String poster_path, String overview, String backdrop_path, int id, float vote_average, double popularity){
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.overview = overview;
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.vote_average = vote_average;
        this.popularity = popularity;
    }

    protected MovieParcelable(Parcel in) {
        this.title = in.readString();
        this.release_date = in.readString();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.backdrop_path = in.readString();
        this.id = in.readInt();
        this.vote_average = in.readFloat();
        this.popularity = in.readDouble();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(backdrop_path);
        dest.writeInt(id);
        dest.writeFloat(vote_average);
        dest.writeDouble(popularity);
    }


    public static final Creator<MovieParcelable> CREATOR = new Creator<MovieParcelable>() {
        @Override
        public MovieParcelable createFromParcel(Parcel in) {
            return new MovieParcelable(in);
        }

        @Override
        public MovieParcelable[] newArray(int size) {
            return new MovieParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
