package com.invima.scaffolding.domain.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


@Entity
@Table(name = "DEPENDENCIA")

public class Dependencia extends AuditActivoModifySupport {

    @Id
    @GenericGenerator(name = "DEPENDENCIA_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "DEPENDENCIA_SEQ")
        ,
			@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPENDENCIA_SEQ")
    @Column(name = "DEP_ID")
    private Integer id;

    @Column(name = "DEP_CODIGO")
    private String codigo;


    @Column(name = "DEP_NOMBRE")
    private String nombre;

    @Column(name = "DEP_SIGLA")
    private String sigla;

    @Column(name = "DEP_PADRE")
    private Integer padre;

    @ManyToOne
    @JoinColumn(name = "USU_ID_JEFE")
    private Usuario jefe;

    @ManyToOne
    @JoinColumn(name = "USU_ID_FIRMA_PRINCIPAL")
    private Usuario firmaPrincipal;

    @Column(name = "DEP_RESOLUCION_JEFE")
    private String depResolucionJefe;

    @ManyToOne
    @JoinColumn(name = "USU_ID_JEFE_ENCARGADO")
    private Usuario jefeEncargado;

    @Column(name = "FCH_INICIO_JEFE_ENCARGADO")
    private Date fchInicioJefeEncargado;

    @Column(name = "FCH_FIN_JEFE_ENCARGADO")
    private Date fchFinJefeEncargado;

    @OneToMany(mappedBy = "dependencia")
    private List<DependenciaTrd> trds;

    @Column(name = "DEP_CODIGO_LDAP")
    private String depCodigoLdap;

    @Column(name = "DEP_CODIGO_ORFEO")
    private String depCodigoOrfeo;

    @Column(name = "DIRECCION")
    private String direccion;

    /*
	 * 2017-02-06 jgarcia@controltechcg.com Issue #123: Nuevo campo CIUDAD en la
	 * tabla de DEPENDENCIA.
     */
    @Column(name = "CIUDAD")
    private String ciudad;

    /*
	 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS: Ordenamiento por peso.
     */
    @Column(name = "DEP_PESO_ORDEN")
    private Integer pesoOrden;

    @Column(name = "DEP_IND_ENVIO_DOCUMENTOS")
    private Boolean depIndEnvioDocumentos;

    @ManyToOne
    @JoinColumn(name = "USU_ID_REGISTRO")
    private Usuario usuarioRegistro;

    /**
     * @author Ss. Alejandro Herrera Montilla - aherreram@imi.mil.co
     * @date 01/04/2020
     * feature-41-gogs Plantillas Categorizadas por Usuario.
     */
	@ManyToMany(mappedBy = "dependenciaCollection")
	private Collection<Plantilla> plantillaCollection;

    @Transient
    private List<Dependencia> subs;

    @Transient
    private String siglaNombre;

    @Transient
    private String idString;

    @Transient
    private String idPadreString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPadre() {
        return padre;
    }

    public void setPadre(Integer padre) {
        this.padre = padre;
    }

    public Usuario getJefe() {
        return jefe;
    }

    public void setJefe(Usuario jefe) {
        this.jefe = jefe;
    }

    public Usuario getFirmaPrincipal() {
        return firmaPrincipal;
    }

    public void setFirmaPrincipal(Usuario firmaPrincipal) {
        this.firmaPrincipal = firmaPrincipal;
    }

    public String getDepResolucionJefe() {
        return depResolucionJefe;
    }

    public void setDepResolucionJefe(String depResolucionJefe) {
        this.depResolucionJefe = depResolucionJefe;
    }

    public Usuario getJefeEncargado() {
        return jefeEncargado;
    }

    public void setJefeEncargado(Usuario jefeEncargado) {
        this.jefeEncargado = jefeEncargado;
    }

    public Date getFchInicioJefeEncargado() {
        return fchInicioJefeEncargado;
    }

    public void setFchInicioJefeEncargado(Date fchInicioJefeEncargado) {
        this.fchInicioJefeEncargado = fchInicioJefeEncargado;
    }

    public Date getFchFinJefeEncargado() {
        return fchFinJefeEncargado;
    }

    public void setFchFinJefeEncargado(Date fchFinJefeEncargado) {
        this.fchFinJefeEncargado = fchFinJefeEncargado;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public List<DependenciaTrd> getTrds() {
        return trds;
    }

    public void setTrds(List<DependenciaTrd> trds) {
        this.trds = trds;
    }

    public String getDepCodigoLdap() {
        return depCodigoLdap;
    }

    public void setDepCodigoLdap(String depCodigoLdap) {
        this.depCodigoLdap = depCodigoLdap;
    }

    public String getDepCodigoOrfeo() {
        return depCodigoOrfeo;
    }

    public void setDepCodigoOrfeo(String depCodigoOrfeo) {
        this.depCodigoOrfeo = depCodigoOrfeo;
    }

    public List<Dependencia> getSubs() {
        return subs;
    }

    public void setSubs(List<Dependencia> subs) {
        this.subs = subs;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSiglaNombre() {
        if (nombre != null && sigla != null) {
            return sigla + " - " + nombre;
        } else {
            siglaNombre = nombre;
        }
        return siglaNombre;
    }

    public String getIdString() {
        if (id != null) {
            idString = id.toString().replaceAll("\\.", "");
        } else {
            idString = null;
        }
        return idString;
    }

    public String getIdPadreString() {
        if (padre != null) {
            idPadreString = padre.toString().replaceAll("\\.", "");
        } else {
            idPadreString = null;
        }
        return idPadreString;
    }

    public void setSiglaNombre(String siglaNombre) {
        this.siglaNombre = siglaNombre;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public void setIdPadreString(String idPadreString) {
        this.idPadreString = idPadreString;
    }

    /**
     * @return the ciudad
     */
    // Issue #123
    public String getCiudad() {
        return ciudad;
    }

    /**
     * @param ciudad the ciudad to set
     */
    // Issue #123
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * @return the Peso Orden
     */
    // Issue #45
    public Integer getPesoOrden() {
        return pesoOrden;
    }

    /**
     * @param pesoOrden
     */
    // Issue #45
    public void setPesoOrden(Integer pesoOrden) {
        this.pesoOrden = pesoOrden;
    }

    /**
     * @return the Indicador de envio documentos
     */
    // Issue #147
    public Boolean getDepIndEnvioDocumentos() {
        return depIndEnvioDocumentos;
    }

    /**
     * @param depIndEnvioDocumentos
     */
    // Issue #147
    public void setDepIndEnvioDocumentos(Boolean depIndEnvioDocumentos) {
        this.depIndEnvioDocumentos = depIndEnvioDocumentos;
    }

    /**
     * Retorna el usuario de registro asignado a la dependencia
     * @return usuatio registro
     */
    // Issue #162
    public Usuario getUsuarioRegistro() {
        return usuarioRegistro;
    }

    /**
     * Setea el usuario registro asignado a la dependencia
     * @param usuarioRegistro 
     */
    // Issue #162
    public void setUsuarioRegistro(Usuario usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public Collection<Plantilla> getPlantillaCollection() {
        return plantillaCollection;
    }

    public void setPlantillaCollection(Collection<Plantilla> plantillaCollection) {
        this.plantillaCollection = plantillaCollection;
    }
}