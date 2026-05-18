package com.grupo5.gettoday;

import com.google.gson.annotations.SerializedName;

/**
 * Cuerpo del PUT /api/auth/usuario/modificar
 */
public class PeticionModificarUsuario {

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("password")
    private String password; // null o "" => el servidor NO cambia la contraseña

    @SerializedName("rol")
    private int rol;

    // Solo si rol == 1
    @SerializedName("nombreLocal")
    private String nombreLocal;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("descripcion")
    private String descripcion;

    public PeticionModificarUsuario(String nombre, String email, String telefono,
                                    String password, int rol,
                                    String nombreLocal, String direccion, String descripcion) {
        this.nombre      = nombre;
        this.email       = email;
        this.telefono    = telefono;
        this.password    = password;
        this.rol         = rol;
        this.nombreLocal = nombreLocal;
        this.direccion   = direccion;
        this.descripcion = descripcion;
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

    public String getPassword() {
        return password;
    }

    public int getRol() {
        return rol;
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