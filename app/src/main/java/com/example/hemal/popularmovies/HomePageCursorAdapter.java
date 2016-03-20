package com.example.hemal.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

/**
 * Created by hemal on 20/3/16.
 */
public abstract class HomePageCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private Context context;
    private Cursor cursor;
    private boolean mDataValid;
    private int rowIDColumn;
    private DataSetObserver dataSetObserver;

    public HomePageCursorAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
        mDataValid = (cursor!=null);
        rowIDColumn = mDataValid ? this.cursor.getColumnIndex("_id") : -1;
        dataSetObserver = new NotifyingDataSetObserver();
        if( this.cursor != null ){
            this.cursor.registerDataSetObserver(dataSetObserver);
        }
    }


    public Cursor getCursor(){
        return this.cursor;
    }


    @Override
    public int getItemCount() {
        return (mDataValid && this.cursor != null) ? this.cursor.getCount() : 0;
    }


    @Override
    public long getItemId(int position) {
        if(mDataValid && this.cursor != null && this.cursor.moveToPosition(position)){
            return this.cursor.getLong(rowIDColumn);
        }
        return 0;
    }


    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }


    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);


    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(!mDataValid){
            throw new IllegalStateException("This should only be called when data is valid!");
        }
        if(!this.cursor.moveToPosition(position)){
            throw new IllegalStateException("Couldn't move to position " + position);
        }

        onBindViewHolder(holder, this.cursor);
    }


    public void changeCursor(Cursor cursor){
        Cursor old = swapCursor(cursor);
        if(old != null){
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor){

        if(newCursor == this.cursor)
            return null;

        final Cursor oldCursor = this.cursor;
        if(oldCursor != null && dataSetObserver != null){
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }

        this.cursor = newCursor;
        if(this.cursor != null){
            if(dataSetObserver != null){
                this.cursor.unregisterDataSetObserver(dataSetObserver);
            }
            rowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        }else{
            rowIDColumn = -1;
            mDataValid  = false;
            notifyDataSetChanged();
        }

        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver{
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
        }
    }
}
































