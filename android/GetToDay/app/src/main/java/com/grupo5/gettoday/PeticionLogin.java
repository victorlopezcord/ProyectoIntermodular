package com.grupo5.gettoday;
import com.google.gson.annotations.SerializedName;

/**
 * Cuerpo del POST /api/auth/login
 */
public class PeticionLogin {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public PeticionLogin(String email, String password) {
        this.email    = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}