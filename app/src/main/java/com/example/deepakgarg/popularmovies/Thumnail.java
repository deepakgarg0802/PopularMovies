package com.example.deepakgarg.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Deepak Garg on 25-06-2016.
 */
public class Thumnail implements Parcelable{
    String movie_id;
    String name;
    String image_url;

    public Thumnail()
    {
        this.name="Refresh to see";
    }

    public Thumnail(String id, String name, String grade){
        this.movie_id = id;
        this.name = name;
        this.image_url = grade;
    }
    public Thumnail(Parcel in)
    {
        String[] data = new String[3];

        in.readStringArray(data);
        this.movie_id = data[0];
        this.name = data[1];
        this.image_url = data[2];
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.movie_id,
                this.name,
                this.image_url});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Thumnail createFromParcel(Parcel in) {
            return new Thumnail(in);
        }

        public Thumnail[] newArray(int size) {
            return new Thumnail[size];
        }
    };
}
