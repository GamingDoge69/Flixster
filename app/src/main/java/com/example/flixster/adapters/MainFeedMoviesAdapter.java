package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import java.util.List;

public class MainFeedMoviesAdapter extends RecyclerView.Adapter<MainFeedMoviesAdapter.ViewHolder>{

    Context context;
    List<Movie> mainFeedMovies;
    final static String TAG = "MainFeedMoviesAdapter";

    public MainFeedMoviesAdapter(Context context, List<Movie> mainFeedMovies) {
        this.context = context;
        this.mainFeedMovies = mainFeedMovies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movieData = mainFeedMovies.get(position);
        holder.bind(movieData);
    }

    @Override
    public int getItemCount() {
        return mainFeedMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPoster;
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);

            // Ensure Rounded Corners are respected
            ivPoster.setClipToOutline(true);

            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvOverview = itemView.findViewById(R.id.tvMovieOverview);
        }


        public void bind(Movie movieData) {
            tvTitle.setText(movieData.getTitle());
            tvOverview.setText(movieData.getOverview());
            Glide.with(context)
                    .load(
                            context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT?
                            movieData.getPosterPath(): movieData.getBackdropPath())
                    .placeholder(R.drawable.loading)
                    .fitCenter()
                    .into(ivPoster);
        }
    }
}
