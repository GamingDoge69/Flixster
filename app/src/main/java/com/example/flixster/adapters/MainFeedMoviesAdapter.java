package com.example.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.example.flixster.DetailActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
        ItemMovieBinding binding;

        RelativeLayout container;
        ImageView ivPoster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMovieBinding.bind(itemView);
            container = binding.movieItemContainer;
            ivPoster = binding.ivPoster;

        }


        public void bind(Movie movieData) {
            binding.setMovie(movieData);
            binding.executePendingBindings();
            Glide.with(context)
                    .load(
                            context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                                    movieData.getPosterPath() : movieData.getBackdropPath())
                    .transform(new FitCenter(), new RoundedCornersTransformation(16, 0))
                    .placeholder(R.drawable.loading)
                    .into(ivPoster);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailActivity.class);

                    Pair<View, String> titlePair = new Pair<>(binding.tvMovieTitle, "movieTitle");
                    Pair<View, String> overviewPair = new Pair<>(binding.tvMovieOverview, "movieOverview");

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, titlePair, overviewPair);

                    i.putExtra("MovieData", Parcels.wrap(movieData));

                    context.startActivity(i, options.toBundle());
                }
            });
        }
    }
}
