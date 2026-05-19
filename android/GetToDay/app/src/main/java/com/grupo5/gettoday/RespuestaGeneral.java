package com.grupo5.gettoday;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class RespuestaGeneral {

    @SerializedName("exito")
    private boolean exito;

    @SerializedName("mensaje")
    private String mensaje;

    // JsonElement en vez de Object: Gson lo parsea de forma lazy
    // sin intentar resolver referencias circulares ni relaciones JPA
    @SerializedName("datos")
    private JsonElement datos;

    public RespuestaGeneral() { }

    public boolean isExito()       { return exito; }
    public String getMensaje()     { return mensaje; }
    public JsonElement getDatos()  { return datos; }
}