package com.example.flixster.models;

import android.view.View;

import com.example.flixster.R;
import com.example.flixster.utils.GenresHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie implements Comparable<Movie>{
    String id;
    String posterPath;
    String backdropPath;
    String title;
    String overview;
    Integer[] genres;
    float rating;

    public Movie(){}

    public Movie(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        rating = (float) jsonObject.getDouble("vote_average");
        genres = extractGenreIds(jsonObject);
    }

    private Integer[] extractGenreIds(JSONObject jsonObject) {
        ArrayList<Integer> genresIds = new ArrayList<>();
        try {
            JSONArray genreIdsAPI = jsonObject.getJSONArray("genre_ids");
            for (int i = 0; i < genreIdsAPI.length(); i++) {
                genresIds.add(genreIdsAPI.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return genresIds.toArray(new Integer[1]);
    }

    public static List<Movie> fromJSONArray(JSONArray moviesJSONArray) throws JSONException {
        List<Movie> movies = new ArrayList<>(moviesJSONArray.length());
        for (int idx = 0; idx < moviesJSONArray.length(); idx++) {
            movies.add(new Movie(moviesJSONArray.getJSONObject(idx)));
        }
        return movies;
    }

    public String getId() {
        return id;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public int visibilityStateOfGenres() {
        return GenresHelper.areGenresReady() && genres.length > 0? View.VISIBLE : View.GONE;
    }

    public String getGenres() {
        return GenresHelper.generateGenreList(genres);
    }

    public String getOverview() {
        return overview;
    }

    public float getRating() {
        return rating;
    }

    public int getRatingDisplayColor() {
        if (rating < 5) {
            return R.color.terrible_movie_red;
        }
        else if (rating < 8) {
            return R.color.average_movie_teal;
        }
        else {
            return R.color.hollywood_gold;
        }
    }

    @Override
    public int compareTo(Movie o) {
        return (int) Math.signum(this.rating - o.rating);
    }
}
