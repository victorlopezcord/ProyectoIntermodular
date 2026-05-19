package com.grupo5.gettoday;

import com.google.gson.annotations.SerializedName;

/**
 * Mapea el campo "datos" de RespuestaGeneral cuando viene del endpoint
 * POST /api/auth/usuario
 *
 * El servidor serializa PeticionUsuario con Jackson. El campo del DTO
 * del servidor se llama "nombre" (igual que en PeticionRegistro y
 * PeticionModificarUsuario), así que el JSON devuelto es:
 * { "nombre": "...", "email": "...", "telefono": "...", ... }
 */
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

    public int    getRol()         { return rol; }
    public String getNombre()      { return nombre; }
    public String getEmail()       { return email; }
    public String getTelefono()    { return telefono; }
    public String getNombreLocal() { return nombreLocal; }
    public String getDireccion()   { return direccion; }
    public String getDescripcion() { return descripcion; }
}