/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.dto;

/**
 *
 * @author kiwitox
 */
public class RespuestaGeneral {
    private boolean exito;
    private String mensaje;
    private Object datos;
    private int rol;

    // Constructor vacío (necesario para que Spring pueda serializar el JSON)
    public RespuestaGeneral() {
    }

    // Constructor con parámetros (el que usamos en el Controller)
    public RespuestaGeneral(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }
    
    public RespuestaGeneral(boolean exito, String mensaje, int rol) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.rol = rol;
    }
    
    public RespuestaGeneral(boolean exito, String mensaje, Object datos) { 
        this.exito = exito; 
        this.mensaje = mensaje; 
        this.datos = datos;
    }

    // Getters y Setters
    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public Object getDatos() {
        return datos;
    }

    public void setDatos(Object datos) {
        this.datos = datos;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
    
}
