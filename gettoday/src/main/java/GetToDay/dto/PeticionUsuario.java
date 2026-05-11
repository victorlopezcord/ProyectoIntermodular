/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.dto;

/**
 *
 * @author kiwitox
 */
public class PeticionUsuario {
    private int rol;
     
    private String nombre;
    private String email;
    private String telefono;
    
    // Campos extra si es negocio
    private String nombreLocal;
    private String direccion;
    private String descripcion;

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
    public PeticionUsuario() {
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
    
    public PeticionUsuario(int rol, String nombre, String email, String telefono, String nombrelocal, String direccion, String descripcion) {
        this.rol = rol;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        
        this.nombreLocal = nombrelocal;
        this.direccion = direccion;
        this.descripcion = descripcion;
        
    }
}
