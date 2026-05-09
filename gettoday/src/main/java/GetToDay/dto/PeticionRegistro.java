/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.dto;

/**
 *
 * @author kiwitox
 */
public class PeticionRegistro {
    public String nombre;
    public String email;
    public String telefono;
    public String password;
    public String rol;
    // Campos extra si es negocio
    public String nombreLocal;
    public String direccion;
    public String descripcion;

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

    public void setRol(String rol) {
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

    public String getRol() {
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
