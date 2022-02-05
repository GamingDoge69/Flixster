package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MainFeedMoviesAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;
import com.example.flixster.utils.GenresHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String GET_MOVIE_NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final String TAG = "MainActivity";

    ActivityMainBinding binding;

    List<Movie> mainFeedMovies = new ArrayList<>();
    RecyclerView rvMainFeedMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        rvMainFeedMovies = binding.rvMainFeedMovies;

        MainFeedMoviesAdapter mainFeedMoviesAdapter = new MainFeedMoviesAdapter(this, mainFeedMovies);
        rvMainFeedMovies.setAdapter(mainFeedMoviesAdapter);
        rvMainFeedMovies.setLayoutManager(new LinearLayoutManager(this));

        GenresHelper.prepareAllGenres();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GET_MOVIE_NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess: Main Feed Data Received");
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONArray movieResultsJsonArray = jsonObject.getJSONArray("results");
                    mainFeedMovies.clear();
                    mainFeedMovies.addAll(Movie.fromJSONArray(movieResultsJsonArray));

                    // Ensure most popular movies at the top
                    Collections.sort(mainFeedMovies, Collections.reverseOrder());

                    mainFeedMoviesAdapter.notifyDataSetChanged();
                    Log.i(TAG, "onSuccess: MainFeedMovies Loaded");
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Failure: ", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}