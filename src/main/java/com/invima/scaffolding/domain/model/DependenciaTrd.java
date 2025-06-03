package com.invima.scaffolding.domain.model;


import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "DEPENDENCIA_TRD")
public class DependenciaTrd extends AuditActivoCreateSupport {

	@Id
	@GenericGenerator(name = "DEPENDENCIA_TRD_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DEPENDENCIA_TRD_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPENDENCIA_TRD_SEQ")
	@Column(name = "DT_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "DEP_ID")
	private Dependencia dependencia;

	@ManyToOne
	@JoinColumn(name = "TRD_ID")
	private Trd trd;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Dependencia getDependencia() {
		return dependencia;
	}

	public void setDependencia(Dependencia dependencia) {
		this.dependencia = dependencia;
	}

	public Trd getTrd() {
		return trd;
	}

	public void setTrd(Trd trd) {
		this.trd = trd;
	}

}
