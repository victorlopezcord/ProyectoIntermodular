/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author kiwitox
 */
@Entity
@Table(name = "Horarios")
@NamedQueries({
    @NamedQuery(name = "Horarios.findAll", query = "SELECT h FROM Horarios h"),
    @NamedQuery(name = "Horarios.findByIdHorario", query = "SELECT h FROM Horarios h WHERE h.idHorario = :idHorario"),
    @NamedQuery(name = "Horarios.findByDiaSemana", query = "SELECT h FROM Horarios h WHERE h.diaSemana = :diaSemana"),
    @NamedQuery(name = "Horarios.findByHoraApertura", query = "SELECT h FROM Horarios h WHERE h.horaApertura = :horaApertura"),
    @NamedQuery(name = "Horarios.findByHoraCierre", query = "SELECT h FROM Horarios h WHERE h.horaCierre = :horaCierre")})
public class Horarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_horario")
    private Integer idHorario;
    @Basic(optional = false)
    @Column(name = "dia_semana")
    private short diaSemana;
    @Basic(optional = false)
    @Column(name = "hora_apertura")
    @Temporal(TemporalType.TIME)
    private Date horaApertura;
    @Basic(optional = false)
    @Column(name = "hora_cierre")
    @Temporal(TemporalType.TIME)
    private Date horaCierre;
    @JoinColumn(name = "id_negocio", referencedColumnName = "id_negocio")
    @ManyToOne(optional = false)
    private Negocios idNegocio;

    public Horarios() {
    }

    public Horarios(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Horarios(Integer idHorario, short diaSemana, Date horaApertura, Date horaCierre) {
        this.idHorario = idHorario;
        this.diaSemana = diaSemana;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public short getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(short diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Date getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(Date horaApertura) {
        this.horaApertura = horaApertura;
    }

    public Date getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(Date horaCierre) {
        this.horaCierre = horaCierre;
    }

    public Negocios getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(Negocios idNegocio) {
        this.idNegocio = idNegocio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHorario != null ? idHorario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horarios)) {
            return false;
        }
        Horarios other = (Horarios) object;
        if ((this.idHorario == null && other.idHorario != null) || (this.idHorario != null && !this.idHorario.equals(other.idHorario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GetToDay.demo.entity.Horarios[ idHorario=" + idHorario + " ]";
    }
    
}
