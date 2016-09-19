package com.example.sheahtaa.popularmovie;

/**
 * Created by Sheahtaa on 8/22/2016.
 */
public class Trailer {
    private String trailerKey;
    private String trailerName;



    public String getTrailrtKey() {
        return trailerKey;
    }

    public void setTrailrtKey(String trailrtKey) {
        this.trailerKey = "https://www.youtube.com/watch?v="+trailrtKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }
}