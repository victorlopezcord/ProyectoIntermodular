package com.grupo5.gettoday;

import com.google.gson.annotations.SerializedName;

public class RespuestaLogin {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("rol")
    private String rol; // "CLIENTE" o "NEGOCIO"

    public RespuestaLogin() { }

    public int getId()        { return id; }
    public String getNombre() { return nombre; }
    public String getEmail()  { return email; }
    public String getRol()    { return rol; }
}