package com.invima.scaffolding.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class AuditActivoModifySupport extends AuditModifySupport {

	@Column(name = "ACTIVO")
	private Boolean activo = Boolean.TRUE;

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

}
