package com.invima.scaffolding.application.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class UsuarioBusquedaDTO implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3347790125394541758L;

    /**
     * Indica si la búsqueda es exitosa o no.
     */
    private Boolean ok;

    /**
     * Mensaje resultado de la búsqueda.
     */
    private String mensajeBusqueda;

    /**
     * Id.
     */
    private Integer id;

    /**
     * Nombre.
     */
    private String nombre;

    /**
     * Grado.
     */
    private String grado;

    /**
     * ID de la clasificación.
     */
    private Integer clasificacionId;

    /**
     * Nombre de la clasificación.
     */
    private String clasificacionNombre;

    /**
     * Nombre de la dependencia.
     */
    private String dependenciaNombre;
    
    // 2018-03-12 edison.gonzalez@controltechcg.com Issue #151 (SIGDI-Controltech):
    // Se añade la lista de cargos del usuario destino
    /**
     * Cargos del usuario.
     */
    private List<CargoDTO> cargosDestino;

    /**
     * Obtiene el estado de búsqueda exitosa.
     *
     * @return {@code true} si la búsqueda es exitosa; de lo contrario,
     * {@code false}.
     */
    public Boolean getOk() {
        return ok;
    }

    /**
     * Establece el estado de búsqueda exitosa.
     *
     * @param ok {@code true} si la búsqueda es exitosa; de lo contrario,
     * {@code false}.
     */
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    /**
     * Obtiene el mensaje de búsqueda.
     *
     * @return Mensaje.
     */
    public String getMensajeBusqueda() {
        return mensajeBusqueda;
    }

    /**
     * Establece el mensaje de búsqueda.
     *
     * @param mensajeBusqueda Mensaje.
     */
    public void setMensajeBusqueda(String mensajeBusqueda) {
        this.mensajeBusqueda = mensajeBusqueda;
    }

    /**
     * Obtiene el Id.
     *
     * @return Id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el Id.
     *
     * @param id Id.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre.
     *
     * @return Nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre.
     *
     * @param nombre Nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el grado.
     *
     * @return Grado.
     */
    public String getGrado() {
        return grado;
    }

    /**
     * Establece el grado.
     *
     * @param grado Grado.
     */
    public void setGrado(String grado) {
        this.grado = grado;
    }

    /**
     * Obtiene el id de la clasificación.
     *
     * @return Id de la clasificación..
     */
    public Integer getClasificacionId() {
        return clasificacionId;
    }

    /**
     * Obtiene el id de la clasificación.
     *
     * @param clasificacionId Id de la clasificación.
     */
    public void setClasificacionId(Integer clasificacionId) {
        this.clasificacionId = clasificacionId;
    }

    /**
     * Obtiene el nombre de la clasificación.
     *
     * @return Nombre de la clasificación.
     */
    public String getClasificacionNombre() {
        return clasificacionNombre;
    }

    /**
     * Establece el nombre de la clasificación.
     *
     * @param clasificacionNombre Nombre de la clasificación.
     */
    public void setClasificacionNombre(String clasificacionNombre) {
        this.clasificacionNombre = clasificacionNombre;
    }

    /**
     * Obtiene los cargos del usuario.
     *
     * @return Lista de cargos.
     */
    public List<CargoDTO> getCargosDestino() {
        return cargosDestino;
    }

    /**
     * Establece los cargos del usuario.
     *
     * @param cargosDestino Cargos
     */
    public void setCargosDestino(List<CargoDTO> cargosDestino) {
        this.cargosDestino = cargosDestino;
    }

    public String getDependenciaNombre() {
        return dependenciaNombre;
    }

    public void setDependenciaNombre(String dependenciaNombre) {
        this.dependenciaNombre = dependenciaNombre;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.ok);
        hash = 13 * hash + Objects.hashCode(this.mensajeBusqueda);
        hash = 13 * hash + Objects.hashCode(this.id);
        hash = 13 * hash + Objects.hashCode(this.nombre);
        hash = 13 * hash + Objects.hashCode(this.grado);
        hash = 13 * hash + Objects.hashCode(this.clasificacionId);
        hash = 13 * hash + Objects.hashCode(this.clasificacionNombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UsuarioBusquedaDTO other = (UsuarioBusquedaDTO) obj;
        if (!Objects.equals(this.mensajeBusqueda, other.mensajeBusqueda)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.grado, other.grado)) {
            return false;
        }
        if (!Objects.equals(this.clasificacionNombre, other.clasificacionNombre)) {
            return false;
        }
        if (!Objects.equals(this.ok, other.ok)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.clasificacionId, other.clasificacionId);
    }

}
