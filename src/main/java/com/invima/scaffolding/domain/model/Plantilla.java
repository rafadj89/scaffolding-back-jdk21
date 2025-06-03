package com.invima.scaffolding.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import java.util.Collection;
import java.util.List;

/**
 * @author mcr
 */
@Entity
@Data
@Table(name = "PLANTILLA")
public class Plantilla extends AuditActivoModifySupport {

    @Id
    @GenericGenerator(name = "PLANTILLA_SEQ", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "PLANTILLA_SEQ"), @Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLANTILLA_SEQ")
    @Column(name = "PLA_ID")
    private Integer id;

    @Column(name = "PLA_CODIGO")
    private String codigo;

    @Column(name = "PLA_CONTENIDO_CABEZA")
    private String contenidoCabeza;

    @Column(name = "PLA_CONTENIDO_PIE")
    private String contenidoPie;

    @Column(name = "PLA_TIPO")
    private String tipo;

    @Column(name = "PLA_NOMBRE")
    private String nombre;

    @Column(name = "PLANTILLA_SISTEMA")
    private boolean plantillaSistema;

    @Column(name = "PLA_ARCHIVO")
    private String archivo;

    @Column(name = "TEXTO_DEFAULT")
    private String textoDefultPlantilla;

    @Column(name = "PLA_DOCX_DOCUMENTO")
    private String docx4jDocumento;

    @Column(name = "BOOK_NAME")
    private String bookmarkName;

    @Column(name = "BOOK_VALUE")
    private String bookmarkValue;

    @Transient
    private String idPlantillaSeleccionada = null;

    /*
        2018-06-27 samuel.delgado@controltechcg.com feature #176: se agrega método para validar
        los wildcards que posee la plantilla.
    */
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "WILDCARD_PV_PLANTILLAS",
            joinColumns = {@JoinColumn(name = "PLA_ID", referencedColumnName = "PLA_ID")},
            inverseJoinColumns = {@JoinColumn(name = "WPV_ID", referencedColumnName = "WPV_ID")}
    )
    private List<WildcardPlantilla> wildCardsPlantilla;

    /**
     * @author Ss. Alejandro Herrera Montilla - aherreram@imi.mil.co
     * @date 01/04/2020
     * feature-41-gogs Plantillas Categorizadas por Usuario.
     */
    @ManyToMany(mappedBy = "plantillaCollection")
    private Collection<Usuario> usuarioCollection;

    /**
     * @author Ss. Alejandro Herrera Montilla - aherreram@imi.mil.co
     * @date 01/04/2020
     * feature-41-gogs Plantillas Categorizadas por Usuario.
     */
    @JoinTable(name = "DEPENDENCIA_X_PLANTILLA", joinColumns = {
            @JoinColumn(name = "PLANTILLA", referencedColumnName = "PLA_ID")}, inverseJoinColumns = {
            @JoinColumn(name = "DEPENDENCIA", referencedColumnName = "DEP_ID")})
    @ManyToMany
    private Collection<Dependencia> dependenciaCollection;

    /**
     * @author Ss. Alejandro Herrera Montilla - aherreram@imi.mil.co
     * @date 01/04/2020
     * feature-41-gogs Plantillas Categorizadas por Usuario.
     */
    @JoinColumn(name = "CATEGORIA", referencedColumnName = "CATEGORIA_ID")
    @ManyToOne
    private CategoriaPlantilla categoriaPlantilla;


    public List<WildcardPlantilla> getWildCards() {
        return wildCardsPlantilla;
    }

    public void setWildCards(List<WildcardPlantilla> wildCardsPlantilla) {
        this.wildCardsPlantilla = wildCardsPlantilla;
    }

    public boolean plantillaTienePlantilla() {
        return docx4jDocumento != null && docx4jDocumento.trim().length() > 0;
    }

    public String toString() {
        return "Plantilla(id=" + this.getId() + ", codigo=" + this.getCodigo()
                + ", tipo=" + this.getTipo() + ", nombre=" + this.getNombre()
                + ", plantillaSistema=" + this.isPlantillaSistema();
    }
}
