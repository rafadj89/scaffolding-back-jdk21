package com.invima.scaffolding.application.dto;

import com.invima.scaffolding.application.util.AppConstants;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @author Rafael G Blanco Banquez <rblanco@asconstroltech.com>
 *
 */
public class UsuarioHistorialFirmaDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date fecha;
	private String usuario;
	private String grado;
	private String nombre;
	
	public UsuarioHistorialFirmaDTO() {
		// TODO Auto-generated constructor stub
	}

	public UsuarioHistorialFirmaDTO(Date fecha, String usuario, String grado, String nombre) {
		super();
		this.fecha = fecha;
		this.usuario = usuario;
		this.grado = grado;
		this.nombre = nombre;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		if (grado != null && !AppConstants.SIN_GRADO.equals(grado))
			b.append(grado).append(". ");
		b.append(nombre);
		return b.toString();
	}
}
