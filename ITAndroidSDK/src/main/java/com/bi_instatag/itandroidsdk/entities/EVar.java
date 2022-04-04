package com.bi_instatag.itandroidsdk.entities;

import com.google.gson.annotations.SerializedName;

public class EVar {
    @SerializedName("evar")
    public String name;

    @SerializedName("fqn")
    public String fqn;

    @SerializedName("value")
    public String value;
}
