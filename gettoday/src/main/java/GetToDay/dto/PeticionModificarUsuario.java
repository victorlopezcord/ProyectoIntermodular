/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.dto;

/**
 *
 * @author kiwitox
 */
public class PeticionModificarUsuario {
    private String nombre;
    private String email;
    private String telefono;
    private String password;
    private int rol;
    // Campos extra si es negocio
    private String nombreLocal;
    private String direccion;
    private String descripcion;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setDescripcion(String descripcion) {
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
    
    public PeticionModificarUsuario() {
    }

    public PeticionModificarUsuario(String nombre, String email, String telefono, String password, int rol, String nombreLocal, String direccion, String descripcion) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.rol = rol;
        this.nombreLocal = nombreLocal;
        this.direccion = direccion;
        this.descripcion = descripcion;
    }
    
}
