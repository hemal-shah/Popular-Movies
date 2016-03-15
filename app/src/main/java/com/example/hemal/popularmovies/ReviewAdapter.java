package com.example.hemal.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hemal on 13/3/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolderReview>{

    Context context;
    ArrayList<ReviewParcelable> arrayList;

    public ReviewAdapter(Context context, ArrayList<ReviewParcelable> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolderReview onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.review_single_row, parent, false);
        return new ViewHolderReview(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderReview holder, int position) {
        ReviewParcelable reviewParcelable = arrayList.get(position);
        holder.author.setText(reviewParcelable.author);
        holder.review.setText(reviewParcelable.review);
    }

    @Override
    public int getItemCount() {
        return ( arrayList == null || arrayList.size() == 0)? 0 : arrayList.size();
    }

    public class ViewHolderReview extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_author_single_row) TextView author;
        @Bind(R.id.tv_review_single_row) TextView review;

        public ViewHolderReview(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
