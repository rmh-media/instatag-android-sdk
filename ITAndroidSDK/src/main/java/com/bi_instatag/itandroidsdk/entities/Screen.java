package com.bi_instatag.itandroidsdk.entities;

import com.bi_instatag.itandroidsdk.entities.EVar;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }

    @SerializedName("evars")
    List<EVar> evars = new ArrayList<EVar>();

    public List<EVar> getEvars() {
        return evars;
    }
}
