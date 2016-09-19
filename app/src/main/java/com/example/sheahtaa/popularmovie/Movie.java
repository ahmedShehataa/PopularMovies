package com.example.sheahtaa.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by Sheahtaa on 8/8/2016.
 */
@SimpleSQLTable(table = "Movie",provider = "MovieProvider")
public class Movie implements Parcelable{
    @SimpleSQLColumn(value = "_id",primary = true)
    private int Id;
    @SimpleSQLColumn(value = "col_poster")
    private String poster;
    @SimpleSQLColumn(value = "col_overView")
    private String overView;
    @SimpleSQLColumn(value = "col_date")
    private String Date;
    @SimpleSQLColumn(value = "col_title")
    private String Title;
    @SimpleSQLColumn(value = "col_vote")
    private String Vote;


    public Movie() {
    }

    public Movie(String poster, String overView, String date, String title, String vote, int id) {
        this.poster = poster;
        this.overView = overView;
        Date = date;
        Title = title;
        Vote = vote;
        Id = id;
    }

    protected Movie(Parcel in) {
        poster = in.readString();
        overView = in.readString();
        Date = in.readString();
        Title = in.readString();
        Vote = in.readString();
        Id = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getVote() {
        return Vote;
    }

    public void setVote(String vote) {
        Vote = vote;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster);
        dest.writeString(overView);
        dest.writeString(Date);
        dest.writeString(Title);
        dest.writeString(Vote);
        dest.writeInt(Id);
    }
}