package com.grupo5.gettoday;

public class PeticionCita {

    private String  emailCliente;
    private Integer idServicio;
    private Integer idCita;
    private String  fecha;
    private String  horaInicio;
    private String  estado;
    private String  emailNegocio;

    // Constructor vacío (necesario para los métodos estáticos)
    public PeticionCita() {}

    // Para HORAS OCUPADAS: idServicio + fecha
    public PeticionCita(Integer idServicio, String fecha) {
        this.idServicio = idServicio;
        this.fecha      = fecha;
    }

    // Para CREAR CITA: email + idServicio + fecha + hora
    public PeticionCita(String emailCliente, int idServicio, String fecha, String horaInicio) {
        this.emailCliente = emailCliente;
        this.idServicio   = idServicio;
        this.fecha        = fecha;
        this.horaInicio   = horaInicio;
    }

    // Para VER CITAS CLIENTE: solo email
    public PeticionCita(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    // ── Fábricas estáticas ────────────────────────────────────────

    /** Para GET /api/citas/negocio: email del dueño */
    public static PeticionCita paraNegocio(String emailNegocio) {
        PeticionCita p = new PeticionCita();
        p.emailNegocio = emailNegocio;
        return p;
    }

    /** Para PUT /api/citas/modificar: id de la cita + nuevo estado */
    public static PeticionCita paraModificar(int idCita, String estado) {
        PeticionCita p = new PeticionCita();
        p.idCita = idCita;
        p.estado = estado;
        return p;
    }

    // ── Getters / Setters ─────────────────────────────────────────

    public String  getEmailCliente()             { return emailCliente; }
    public void    setEmailCliente(String e)     { this.emailCliente = e; }

    public Integer getIdServicio()               { return idServicio; }
    public void    setIdServicio(Integer i)      { this.idServicio = i; }

    public Integer getIdCita()                   { return idCita; }
    public void    setIdCita(Integer i)          { this.idCita = i; }

    public String  getFecha()                    { return fecha; }
    public void    setFecha(String f)            { this.fecha = f; }

    public String  getHoraInicio()               { return horaInicio; }
    public void    setHoraInicio(String h)       { this.horaInicio = h; }

    public String  getEstado()                   { return estado; }
    public void    setEstado(String e)           { this.estado = e; }

    public String  getEmailNegocio()               { return emailNegocio; }
    public void    setEmailNegocio(String e)       { this.emailNegocio = e; }
}