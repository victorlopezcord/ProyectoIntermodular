package com.grupo5.gettoday;

import com.google.gson.annotations.SerializedName;

public class PeticionEmail {
    @SerializedName("email")
    private String email;

    public PeticionEmail(String email) {
        this.email = email;
    }
}