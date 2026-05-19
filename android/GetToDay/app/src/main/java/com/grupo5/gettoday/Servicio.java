package com.grupo5.gettoday;

import com.google.gson.annotations.SerializedName;

public class Servicio {

    @SerializedName("idServicio")
    private int idServicio;

    @SerializedName("nombreServicio")
    private String nombreServicio;

    @SerializedName("precio")
    private double precio;

    public Servicio() {}

    public Servicio(int idServicio, String nombreServicio, double precio) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.precio = precio;
    }

    public int getIdServicio()                           { return idServicio; }
    public void setIdServicio(int idServicio)            { this.idServicio = idServicio; }
    public String getNombreServicio()                    { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }
    public double getPrecio()                            { return precio; }
    public void setPrecio(double precio)                 { this.precio = precio; }
}