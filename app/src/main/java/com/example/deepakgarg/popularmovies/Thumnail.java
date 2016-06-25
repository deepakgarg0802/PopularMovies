package com.example.deepakgarg.popularmovies;

/**
 * Created by Deepak Garg on 25-06-2016.
 */
public class Thumnail {
    String name="Refresh to see";
    String movie_id;
    String image_url;

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setName(String name) {
        this.name = name;
    }

}
