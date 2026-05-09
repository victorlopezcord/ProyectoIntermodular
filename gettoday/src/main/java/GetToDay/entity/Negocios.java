/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author kiwitox
 */
@Entity
@Table(name = "Negocios")
@NamedQueries({
    @NamedQuery(name = "Negocios.findAll", query = "SELECT n FROM Negocios n"),
    @NamedQuery(name = "Negocios.findByIdNegocio", query = "SELECT n FROM Negocios n WHERE n.idNegocio = :idNegocio"),
    @NamedQuery(name = "Negocios.findByNombreLocal", query = "SELECT n FROM Negocios n WHERE n.nombreLocal = :nombreLocal"),
    @NamedQuery(name = "Negocios.findByDireccion", query = "SELECT n FROM Negocios n WHERE n.direccion = :direccion")})
public class Negocios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_negocio")
    private Integer idNegocio;
    @Basic(optional = false)
    @Column(name = "nombre_local")
    private String nombreLocal;
    @Column(name = "direccion")
    private String direccion;
    @Lob
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNegocio")
    private Collection<Servicios> serviciosCollection;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @OneToOne(optional = false)
    @JsonIgnore
    private Usuarios idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNegocio")
    private Collection<Empleados> empleadosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNegocio")
    private Collection<Horarios> horariosCollection;

    public Negocios() {
    }

    public Negocios(Integer idNegocio) {
        this.idNegocio = idNegocio;
    }

    public Negocios(Integer idNegocio, String nombreLocal) {
        this.idNegocio = idNegocio;
        this.nombreLocal = nombreLocal;
    }

    public Integer getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(Integer idNegocio) {
        this.idNegocio = idNegocio;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<Servicios> getServiciosCollection() {
        return serviciosCollection;
    }

    public void setServiciosCollection(Collection<Servicios> serviciosCollection) {
        this.serviciosCollection = serviciosCollection;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Collection<Empleados> getEmpleadosCollection() {
        return empleadosCollection;
    }

    public void setEmpleadosCollection(Collection<Empleados> empleadosCollection) {
        this.empleadosCollection = empleadosCollection;
    }

    public Collection<Horarios> getHorariosCollection() {
        return horariosCollection;
    }

    public void setHorariosCollection(Collection<Horarios> horariosCollection) {
        this.horariosCollection = horariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNegocio != null ? idNegocio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Negocios)) {
            return false;
        }
        Negocios other = (Negocios) object;
        if ((this.idNegocio == null && other.idNegocio != null) || (this.idNegocio != null && !this.idNegocio.equals(other.idNegocio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GetToDay.demo.entity.Negocios[ idNegocio=" + idNegocio + " ]";
    }
    
}
