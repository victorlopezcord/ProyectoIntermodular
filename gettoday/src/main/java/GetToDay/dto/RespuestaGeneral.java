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

    // Constructor vacío (necesario para que Spring pueda serializar el JSON)
    public RespuestaGeneral() {
    }

    // Constructor con parámetros (el que usamos en el Controller)
    public RespuestaGeneral(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
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
    
}
