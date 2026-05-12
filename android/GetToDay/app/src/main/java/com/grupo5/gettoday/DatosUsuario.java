package com.grupo5.gettoday;

import com.google.gson.annotations.SerializedName;

// Clase que guarda los datos del usuario que nos devuelve el servidor
public class DatosUsuario {

    @SerializedName("rol")
    private int rol;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("telefono")
    private String telefono;

    // Solo rellenados si rol == 1 (negocio)
    @SerializedName("nombreLocal")
    private String nombreLocal;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("descripcion")
    private String descripcion;

    public DatosUsuario() { }

    public int getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}