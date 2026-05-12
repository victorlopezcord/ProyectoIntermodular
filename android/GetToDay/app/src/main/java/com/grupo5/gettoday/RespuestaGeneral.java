package com.grupo5.gettoday;

import com.google.gson.annotations.SerializedName;

public class RespuestaGeneral {

    @SerializedName("exito")
    private boolean exito;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("datos")
    private Object datos; // Gson lo deserializa como Double cuando es un número

    public RespuestaGeneral() { }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Object getDatos() {
        return datos;
    }
}