package com.example.flixster.utils;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Headers;

public class GenresHelper {
    static private final String GET_MOVIE_GENRES_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    static private final String TAG = "GenresHelper";
    static private final HashMap<Integer, String> GENRE_PARINGS = new HashMap<>();
    static private boolean areGenresReady = false;

    public static void prepareAllGenres() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GET_MOVIE_GENRES_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess: Main Feed Data Received");
                JSONObject jsonObject = json.jsonObject;
                if (statusCode != 200) {
                    return;
                }
                try {
                    JSONArray movieGenresJsonArray = jsonObject.getJSONArray("genres");
                    for (int i = 0; i < movieGenresJsonArray.length(); i++) {
                        GENRE_PARINGS.put(
                                movieGenresJsonArray.getJSONObject(i).getInt("id"),
                                movieGenresJsonArray.getJSONObject(i).getString("name")
                        );
                    }
                    Log.i(TAG, "onSuccess: Genres loaded");
                    areGenresReady = true;
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

    public static boolean areGenresReady() {
        return areGenresReady;
    }

    public static String getGenreName(int genreId) {
        return GENRE_PARINGS.containsKey(genreId)? GENRE_PARINGS.get(genreId) : null;
    }

    public static String generateGenreList(Integer[] genreIds) {
        StringBuilder genresString = new StringBuilder();
        for (int id : genreIds) {
            String genre = GenresHelper.getGenreName(id);
            if (genre == null) continue;
            genresString.append(genre);
            genresString.append(", ");
        }
        if (genresString.length() > 0) {
            genresString.setLength(genresString.length() - 2);
        }
        return genresString.toString();
    }
}
