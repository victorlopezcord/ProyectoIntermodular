package com.grupo5.gettoday;

public class PeticionServicio {

    private String email;
    private Integer idServicio;
    private Integer idNegocio;
    private String nombreServicio;
    private int precio;

    // Para LISTAR negocios: sin body
    public PeticionServicio() {}

    // Para LISTAR servicios del negocio propio: solo email
    public PeticionServicio(String email) {
        this.email = email;
    }

    // Para LISTAR servicios por negocio (cliente): solo idNegocio
    public PeticionServicio(int idNegocio, boolean esPorNegocio) {
        this.idNegocio = idNegocio;
    }

    // Para CREAR: email + nombre + precio
    public PeticionServicio(String email, String nombreServicio, int precio) {
        this.email = email;
        this.nombreServicio = nombreServicio;
        this.precio = precio;
    }

    // Para MODIFICAR: id + nombre + precio
    public PeticionServicio(int idServicio, String nombreServicio, int precio) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.precio = precio;
    }

    // Para ELIMINAR: solo idServicio
    public PeticionServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getEmail()                        { return email; }
    public void setEmail(String email)              { this.email = email; }
    public Integer getIdServicio()                  { return idServicio; }
    public void setIdServicio(Integer idServicio)   { this.idServicio = idServicio; }
    public Integer getIdNegocio()                   { return idNegocio; }
    public void setIdNegocio(Integer idNegocio)     { this.idNegocio = idNegocio; }
    public String getNombreServicio()               { return nombreServicio; }
    public void setNombreServicio(String n)         { this.nombreServicio = n; }
    public int getPrecio()                          { return precio; }
    public void setPrecio(int precio)               { this.precio = precio; }
}