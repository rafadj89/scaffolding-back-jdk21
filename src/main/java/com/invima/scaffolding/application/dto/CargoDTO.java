package com.invima.scaffolding.application.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author egonzalezm
 */
public class CargoDTO implements Serializable {

    private static final long serialVersionUID = 7199246451141255067L;

    private int id;
    private String nombre;

    private int RESULT_COUNT;

    public CargoDTO() {
    }

    public CargoDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.nombre);
        return hash;
    }


    public int getRESULT_COUNT() {
        return RESULT_COUNT;
    }

    public void setRESULT_COUNT(int RESULT_COUNT) {
        this.RESULT_COUNT = RESULT_COUNT;
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
        final CargoDTO other = (CargoDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }
}
