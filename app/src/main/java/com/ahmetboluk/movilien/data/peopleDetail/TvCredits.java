package com.ahmetboluk.movilien.data.peopleDetail;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TvCredits implements Serializable {

    @SerializedName("cast")
    @Expose
    private List<Cast_> cast = null;
    @SerializedName("crew")
    @Expose
    private List<Object> crew = null;

    public List<Cast_> getCast() {
        return cast;
    }

    public void setCast(List<Cast_> cast) {
        this.cast = cast;
    }

    public List<Object> getCrew() {
        return crew;
    }

    public void setCrew(List<Object> crew) {
        this.crew = crew;
    }

}