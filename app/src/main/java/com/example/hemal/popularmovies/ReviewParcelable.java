package com.example.hemal.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hemal on 13/3/16.
 */
public class ReviewParcelable implements Parcelable {

    /**
     * While viewing the reviews to any particular movie, I want the user to see
     * two content items as follows:
     * 1) The author
     * 2) The review
     */

    String review, author;

    public ReviewParcelable(String review, String author) {
        this.review = review;
        this.author = author;
    }

    protected ReviewParcelable(Parcel in) {
        this.review = in.readString();
        this.author = in.readString();
    }

    public static final Creator<ReviewParcelable> CREATOR = new Creator<ReviewParcelable>() {
        @Override
        public ReviewParcelable createFromParcel(Parcel in) {
            return new ReviewParcelable(in);
        }

        @Override
        public ReviewParcelable[] newArray(int size) {
            return new ReviewParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(review);
        dest.writeString(author);
    }
}
