package com.exoticdevs.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exoticdevs.data.MoviesContract;
import com.exoticdevs.helper.PicassoCache;
import com.exoticdevs.moviesapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.exoticdevs.moviesapp.R.id.trailer_image;
import static com.exoticdevs.moviesapp.R.id.trailer_name;

/**
 * Created by mac on 4/13/16.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    final private TrailersAdapterOnClickHandler mClickHandler;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final View mView;
        @Bind(trailer_image) ImageView mImageView;
        @Bind(trailer_name) TextView trailerName;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, mView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor, this);
        }
    }

    public static interface TrailersAdapterOnClickHandler {
        void onClick(Cursor cursor, ViewHolder vh);
    }

    public TrailersAdapter(Context mContext, TrailersAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        mCursor.moveToPosition(position);

        String trailerPoster =
                mCursor.getString(mCursor.getColumnIndex(MoviesContract.TrailerEntry.COLUMN_KEY));

        String trailerTitle =
                mCursor.getString(mCursor.getColumnIndex(MoviesContract.TrailerEntry.COLUMN_NAME));

        PicassoCache.getPicassoInstance(mContext)
                .load("http://img.youtube.com/vi/" + trailerPoster + "/0.jpg")
                .into(viewHolder.mImageView);

        viewHolder.trailerName.setText(trailerTitle);
    }


    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }
}
