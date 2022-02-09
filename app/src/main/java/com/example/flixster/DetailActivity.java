package com.example.flixster;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityDetailBinding;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    private static final String TAG = "DetailActivity";

    private static final String YOUTUBE_API_KEY = BuildConfig.YOUTUBE_API_KEY;
    private static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";

    Movie movieData;

    ActivityDetailBinding binding;

    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        movieData = Parcels.unwrap(getIntent().getParcelableExtra("MovieData"));

        binding.setMovie(movieData);
        setRatingBarColor(movieData.getRatingDisplayColor());

        youTubePlayerView = binding.youtubePlayer;


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movieData.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONArray movieVideoResultsJsonArray = jsonObject.getJSONArray("results");
                    if (movieVideoResultsJsonArray.length() == 0) {
                        return;
                    }
                    String trailer_key = movieVideoResultsJsonArray.getJSONObject(0).getString("key");
                    init_youtube(trailer_key);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Failure: ", e);
                }

            }

            private void init_youtube(String trailer_key) {
                youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(trailer_key);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    private void setRatingBarColor(int ratingDisplayColor) {
            ((LayerDrawable) binding.tvRatingBar.getProgressDrawable())
                    .getDrawable(1)
                    .setColorFilter(
                            ContextCompat.getColor(this, ratingDisplayColor),
                            PorterDuff.Mode.SRC_ATOP
                    );
        ((LayerDrawable) binding.tvRatingBar.getProgressDrawable())
                .getDrawable(2)
                .setColorFilter(
                        ContextCompat.getColor(this, ratingDisplayColor),
                        PorterDuff.Mode.SRC_ATOP
                );
    }
}