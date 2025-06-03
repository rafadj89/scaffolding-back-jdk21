package com.invima.scaffolding.domain.model;

import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
public class AuditCreateSupport {

	@Column(name = "CUANDO", updatable = false, insertable = true, nullable = false)
	@CreatedDate
	private Date cuando;

	@Column(name = "QUIEN", updatable = false, insertable = true, nullable = false)
	@CreatedBy
	private Integer quien;

	public Date getCuando() {
		return cuando;
	}

	public void setCuando(Date cuando) {
		this.cuando = cuando;
	}

	public Integer getQuien() {
		return quien;
	}

	public void setQuien(Integer quien) {
		this.quien = quien;
	}

}
