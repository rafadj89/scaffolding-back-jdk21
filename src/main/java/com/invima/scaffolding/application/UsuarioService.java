package com.invima.scaffolding.application;

import com.invima.scaffolding.domain.model.Dependencia;
import com.invima.scaffolding.domain.model.Usuario;
import com.invima.scaffolding.infrastructure.adaptador.UsuarioRepository;
import com.invima.scaffolding.infrastructure.adaptador.UsuarioSpecificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Servicio para las operaciones de negocio de usuario.
 *
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017 Issue #78 (SICDI-Controltech) feature-78
 */
@Service
public class UsuarioService {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UsuarioService.class);

    public final static Integer NOTIFICACION_USUARIO_ACTIVADO = 301;
    public final static Integer NOTIFICACION_USUARIO_INACTIVO = 300;
    public final static Integer NOTIFICACION_USUARIO_INACTIVO_RECURRENTE = 302;

    /**
     * Repositorio de usuarios.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Servicios de dependencias.
     */
    @Autowired
    private DependenciaService dependenciaService;

    /**
     * Mapa de unidades por dependencia.
     */
    private Map<Integer, Dependencia> unidadesMap = new LinkedHashMap<>();

    /**
     * Repositorio de especificación de usuarios.
     */
    @Autowired
    private UsuarioSpecificationRepository repository;

    /**
     * Repositorio de documentos.
     */
    @Autowired
    private DocumentoRepository documentoRepository;

    /**
     * Repositorio de procesos.
     */
    @Autowired
    ProcesoService procesoService;

    @Autowired
    private RazonInhabilitarService razonInhabilitarService;

  //  @Autowired
  //  private NotificacionService notificacionService;

    @Autowired
    private DocumentoActaService documentoActaService;

    @Autowired
    private LdapService ldapService;

    /**
     * Obtiene una página de usuarios correspondientes a la criteria de búsqueda
     * asignada, según las reglas de búsqueda para el tipo de finder de usuario.
     *
     * @param criteria              Criteria de búsqueda.
     * @param pageIndex             Índice de la página.
     * @param pageSize              Tamaño de la página.
     * @param usuarioFinderTipo     Tipo de finder de usuario. En caso de ser
     *                              {@link UsuarioFinderTipo#ACTA} únicamente se presentan los usuarios con
     *                              igual o mayor grado de clasificación que el usuario en sesión.
     * @param usuarioSesion         Usuario en sesión.
     * @param criteriaParametersMap Mapa de otros parámetros para la criteria de
     *                              búsqueda.
     * @return Página de usuarios.
     */
    /*
     * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Adición de usuarioFinderTipo y usuarioSesion.
     */
    public Page<Usuario> findAllByCriteriaSpecification(final String criteria, final int pageIndex, final int pageSize, final UsuarioFinderTipo usuarioFinderTipo,
                                                        final Usuario usuarioSesion, final Map<String, ?> criteriaParametersMap) {

        Specification<Usuario> where = Specification.where(UsuarioSpecifications.inicio());

        /*
         * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Filtro por orden de clasificación para finder tipo ACTA.
         *
         * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: No listar el usuario en sesión para el finder tipo ACTA.
         */
        if (usuarioFinderTipo.equals(UsuarioFinderTipo.ACTA)) {
            where = where.and(UsuarioSpecifications.noSeleccionarUsuario(usuarioSesion));

            final String pin = (String) criteriaParametersMap.get("pin");
            final Documento documento = documentoRepository.findOneByInstanciaId(pin);
            final Clasificacion clasificacion = documento.getClasificacion();
            where = where.and(UsuarioSpecifications.condicionPorOrdenGradoClasificacion(clasificacion.getOrden()));
        }

        if (criteria != null) {
            final String[] tokens = criteria.replaceAll("'", " ").split(" ");
            for (String token : tokens) {
                where = where.and(UsuarioSpecifications.filtrosPorToken(token));
            }
        }
        /*
         * 2017-11-10 edison.gonzalez@controltechcg.com Issue #131
         * (SICDI-Controltech) feature-131: Cambio en la entidad usuario, se
         * coloca llave foranea el grado.
         *
         * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Orden de los nombres cuando los usuarios tienen el mismo
         * grado.
         */


        //final Sort sort = new Sort(
        //        new Sort.Order(Sort.Direction.DESC, "usuGrado.pesoOrden"),
        //        new Sort.Order(Sort.Direction.ASC, "nombre")
        //);

        //Actualización Spring 2
        final Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "usuGrado.pesoOrden"),
                new Sort.Order(Sort.Direction.ASC, "nombre"));

        //final PageRequest pageRequest = new PageRequest(pageIndex, pageSize, orden);
        // Actualización Spring 2
        final PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);
        final Page<Usuario> users = repository.findAll(where, pageRequest);
        return users;
    }

    /**
     * Busca un usuario.
     *
     * @param id ID.
     * @return Usuario.
     */
    public Usuario findOne(Integer id) {
        //return usuarioRepository.findOne(id);
        //Actualización Spring 2
        return usuarioRepository.findById(id).orElse(null);
    }

    /**
     * Obtiene la información básica del usuario (Grado, Nombre, Cargo,
     * Dependencia).
     *
     * @param usuario Usuario.
     * @return Información básica, o texto vacío en caso que el usuario sea
     * {@code null}.
     */
    public String mostrarInformacionBasica(Usuario usuario) {
        if (usuario == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        /*
         * 2017-11-10 edison.gonzalez@controltechcg.com Issue #131
         * (SICDI-Controltech) feature-131: Cambio en la entidad usuario, se
         * coloca llave foranea el grado.
         */
        final String grado = usuario.getUsuGrado().getId();
        if (grado != null && !grado.trim().isEmpty()) {
            builder.append(grado).append(". ");
        }

        final String nombre = usuario.getNombre();
        builder.append(nombre).append(" ");

        /*
         * 2018-02-13 edison.gonzalez@controltechcg.com Issue #149
         * (SICDI-Controltech) feature-149: Se coloca en comentarios la columna
         * de cargo, la cual se remplaza por la columna usuCArgoPrincipalId.
         */
        final String cargo = (usuario.getUsuCargoPrincipalId() != null) ? usuario.getUsuCargoPrincipalId().getCarNombre() : null;
        if (cargo != null && !cargo.trim().isEmpty()) {
            builder.append(" - ").append(cargo).append(" ");
        }

        final Dependencia dependencia = usuario.getDependencia();
        if (dependencia != null) {
            final Dependencia unidad = buscarUnidad(dependencia);
            /*
             * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Se
             * realiza la validacion para que muestre el nombre de la
             * dependencia en caso de que la sigla sea nula.
             */
            if (unidad.getSigla() == null || unidad.getSigla().trim().length() == 0) {
                builder.append("(").append(unidad.getNombre()).append(")");
            } else {
                builder.append("(").append(unidad.getSigla()).append(")");
            }
        }

        return builder.toString().trim();
    }

    /**
     * Obtiene la información básica del usuario (Grado, Nombre, Cargo) en las
     * bandejas.
     *
     * @param usuario Usuario.
     * @return Información básica, o texto vacío en caso que el usuario sea
     * {@code null}.
     */
    public String mostrarInformacionBasicaBandejas(Usuario usuario) {
        if (usuario == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        /*
         * 2017-11-10 edison.gonzalez@controltechcg.com Issue #131
         * (SICDI-Controltech) feature-131: Cambio en la entidad usuario, se
         * coloca llave foránea el grado.
         *
         * 2017-10-23 edison.gonzalez@controltechcg.com Issue #132
         * (SICDI-Controltech) feature-132: Se quita la dependencia, para
         * mostrarla en otra columna
         */
        final String grado = usuario.getUsuGrado().getId();
        if (grado != null && !grado.trim().isEmpty()) {
            builder.append(grado).append(". ");
        }

        final String nombre = usuario.getNombre();
        builder.append(nombre).append(" ");

        /*
         * 2018-02-13 edison.gonzalez@controltechcg.com Issue #149
         * (SICDI-Controltech) feature-149: Se coloca en comentarios la columna
         * de cargo, la cual se remplaza por la columna usuCArgoPrincipalId.
         */
        final String cargo = (usuario.getUsuCargoPrincipalId() != null) ? usuario.getUsuCargoPrincipalId().getCarNombre() : null;
        if (cargo != null && !cargo.trim().isEmpty()) {
            builder.append("</br>").append(cargo);
        }
        return builder.toString().trim();
    }

    /**
     * Obtiene la unidad del usuario (Dependencia).
     *
     * @param usuario Usuario.
     * @return Información básica, o texto vacío en caso que el usuario sea
     * {@code null}.
     */
    public String mostrarInformacionUnidad(Usuario usuario) {
        if (usuario == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        /*
         * 2017-10-23 edison.gonzalez@controltechcg.com Issue #132
         * (SICDI-Controltech) feature-132: Se Agrega la dependencia, para
         * mostrarla en otra columna
         */
        final Dependencia dependencia = usuario.getDependencia();
        if (dependencia != null) {
            final Dependencia unidad = buscarUnidad(dependencia);
            /*
             * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Se
             * realiza la validacion para que muestre el nombre de la
             * dependencia en caso de que la sigla sea nula.
             */
            if (unidad.getSigla() == null || unidad.getSigla().trim().length() == 0) {
                builder.append(unidad.getNombre());
            } else {
                builder.append(unidad.getSigla());
            }
        }

        return builder.toString().trim();
    }

    /**
     * Busca la unidad de la dependencia.
     *
     * @param dependencia Dependencia.
     * @return Dependencia unidad.
     */
    private Dependencia buscarUnidad(Dependencia dependencia) {
        final Integer dependenciaID = dependencia.getId();
        if (unidadesMap.containsKey(dependenciaID)) {
            return unidadesMap.get(dependenciaID);
        }

        final Dependencia unidad = dependenciaService.buscarUnidad(dependencia);
        if (unidad != null)
            unidadesMap.put(dependenciaID, unidad);
        return unidad;
    }

    public Boolean verificaAccesoDocumento(Integer usuId, String pinId) {
        try {
            Integer permiso = documentoRepository.verificaAccesoDocumento(usuId, pinId);
            if (permiso >= 1) {
                return true;
            } else {
                return verficaAccesoDocumentoCorrelacionado(usuId, pinId);
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si un usuario tiene acceso a un documento dentro del ofs
     *
     * @param usuario usuario que quiere acceder
     * @param pinId   instancia del documentos al que quiere acceder
     * @return {@code true} si el usuario tiene acceso; de lo
     * * contrario, {@code false}.
     */
    public Boolean verificaAccesoDocumentoOfs(Usuario usuario, String pinId, Documento documento) {
        if (documento.getInstancia().getEstado().equals(Estado.ANULADO))
            return false;
        try {
            Integer permiso = documentoRepository.verificaAccesoDocumento(usuario.getId(), pinId);
            if (permiso >= 1) {
                return true;
            } else {
                if (verficaAccesoDocumentoCorrelacionado(usuario.getId(), pinId))
                    return true;
                return documentoActaService.verificaAccesoDocumentoActa(usuario, pinId) || documentoActaService.puedeConsultarPorAsociacionOArchivo(usuario, documento);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si el usuario tiene acceso al documento acta.
     *
     * @param usuario            Usuario.
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @return {@code true} si el usuario tiene acceso al documento acta; de lo
     * contrario, {@code false}.
     */
    /*
     * 2018-05-15 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public boolean verificaAccesoDocumentoActa(final Usuario usuario, final String procesoInstanciaID) {
        final BigDecimal acceso = documentoRepository.verificaAccesoDocumentoActa(usuario.getId(), procesoInstanciaID);
        return acceso.equals(BigDecimal.ONE);
    }

    /**
     * Busca un usuario activo por su número de documento.
     *
     * @param documento Número de documento.
     * @return Instancia del usuario activo del documento, o {@code null} en
     * caso de no existir correspondencia en el sistema.
     */
    /*
     * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Usuario buscarActivoPorDocumento(final String documento) {
        return usuarioRepository.findByActivoTrueAndDocumento(documento);
    }

    private boolean verficaAccesoDocumentoCorrelacionado(Integer usuId, String pinId) {
        Instancia i = procesoService.instancia(pinId);
        // Obtiene el identificador de documento
        String docId = i.getVariable(Documento.DOC_ID);
        //Verifica si el documento existe, si no le permite seguir, ya que se está creando
        if (!StringUtils.isBlank(docId)) {
            //Documento doc = documentoRepository.getOne(docId);
            //Actualización Spring 2
            Documento doc = documentoRepository.getReferenceById(docId);
            if (doc.getInstancia().getEstado().equals(Estado.ANULADO))
                return false;
            //Verifica que tenga documento relacionado
            if (doc != null && !StringUtils.isBlank(doc.getRelacionado())) {
                //Documento relacionado = documentoRepository.getOne(doc.getRelacionado());
                //Actualización Spring 2
                Documento relacionado = documentoRepository.getReferenceById(doc.getRelacionado());
                if (relacionado != null) {
                    Integer permiso = documentoRepository.verificaAccesoDocumento(usuId, relacionado.getInstancia().getId());
                    return permiso >= 1;
                }
            }
            return false;

        } else {
            return true;
        }
    }

    /**
     * Metodo que permite retornar el usuario de registro, de acuerdo al usuario
     * quien creo el acta
     *
     * @param usuarioSesion Usuario creador de la acta
     * @return Usuario de registro
     */
    /*
     * 2018-06-22 edison.gonzalez@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Usuario retornaUsuarioRegistro(Usuario usuarioSesion) {
        Dependencia unidadPadre = dependenciaService.buscarUnidad(usuarioSesion.getDependencia());
        return unidadPadre.getUsuarioRegistro();
    }

    /*
     * 2018-08-15 samuel.delgado@controltechcg.com Issue #7 (SICDI-Controltech)
     * feature-gogs-7: metodos para activar (habilitarUsuario) o desactivar usuario (inhabilitarUsuario)
     */

    /**
     * *
     * Método para inhabilitar un usuario.
     *
     * @param usuarioSesion Usuario en sesión
     * @param idRazon       id de la razón por la cual se inhabilita
     */
    public void inhabilitarUsuario(final Usuario usuarioSesion, final Integer idRazon) {
        RazonInhabilitar razon = razonInhabilitarService.findOne(idRazon);
        usuarioSesion.setUsuActivo(Boolean.FALSE);
        usuarioSesion.setRazonInhabilitar(razon);
        usuarioRepository.saveAndFlush(usuarioSesion);

        Map<String, Object> model = new HashMap<>();

        if (usuarioSesion.getDependencia().getJefe() != null) {
            model.put("usuario", usuarioSesion);
            model.put("jefe", usuarioSesion.getDependencia().getJefe());
            model.put("razon", razon);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            model.put("fecha", dateFormat.format(new Date()));

            try {
                Integer numeroInactividades = usuarioRepository.getCountUsuarioCambiosActivoSemana(usuarioSesion.getId());
                if (numeroInactividades == 3) {
                //    notificacionService.enviarNotificacion(model, NOTIFICACION_USUARIO_INACTIVO_RECURRENTE, usuarioSesion.getDependencia().getJefe());
                } else {
                //    notificacionService.enviarNotificacion(model, NOTIFICACION_USUARIO_INACTIVO, usuarioSesion.getDependencia().getJefe());
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * *
     * Método para habilitar un usuario.
     *
     * @param usuarioSesion Usuario en sesión
     */
    public void habilitarUsuario(final Usuario usuarioSesion) {
        usuarioSesion.setUsuActivo(Boolean.TRUE);
        usuarioSesion.setRazonInhabilitar(null);
        usuarioRepository.saveAndFlush(usuarioSesion);

        Map<String, Object> model = new HashMap();

        if (usuarioSesion.getDependencia().getJefe() != null) {
            model.put("usuario", usuarioSesion);
            model.put("jefe", usuarioSesion.getDependencia().getJefe());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            model.put("fecha", dateFormat.format(new Date()));

            try {
            //    notificacionService.enviarNotificacion(model, NOTIFICACION_USUARIO_ACTIVADO, usuarioSesion.getDependencia().getJefe());
            } catch (Exception ex) {
                LOG.error(null, ex);
            }
        }
    }

    /**
     * Método para cambiar la firma de un usuario
     *
     * @param usuario       usuario a cambiar la firma
     * @param usuarioSesion usuario que cambia la firma
     * @param file          firma a cambiar.
     */
    public void cambiarFirmaUsuario(final Usuario usuario, final Usuario usuarioSesion, final String file) {
        usuario.setImagenFirma(file);
        usuario.setImagenFirmaExtension(file + ".png");
        usuario.setCuandoMod(new Date());
        usuario.setQuienMod(usuarioSesion.getId());
        usuarioRepository.saveAndFlush(usuario);
    }

    public Usuario getOne(Integer id) {
        //return usuarioRepository.getOne(id);
        //Actualización Spring 2
        return usuarioRepository.getReferenceById(id);
    }

    public Usuario findByLoginAndActivo(String login, Boolean activo) {
        return usuarioRepository.findByLoginAndActivo(login, activo);
    }

    public Usuario getByLoginAndActivoTrue(String name) {
        return usuarioRepository.getByLoginAndActivoTrue(name);
    }

    public List<Usuario> findByDependenciaAndActivoTrueOrderByGradoDesc(Integer dep) {
        return usuarioRepository.findByDependenciaAndActivoTrueOrderByGradoDesc(dep);
    }

    public Usuario save(Usuario u) {
        return usuarioRepository.save(u);
    }

    public List<Usuario> getUsuariosNuevos(List<Usuario> nuevos, Usuario logged) {
        List<Usuario> nuevosRegistro = new ArrayList<>();
        for (Usuario us : nuevos) {
            boolean flag = true;
            for (UsuSelFavoritos favo : logged.getUsuSelFavoritosList()) {
                if (us.getId().equals(favo.getUsuFav().getId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) nuevosRegistro.add(us);
        }
        return nuevosRegistro;
    }

    public List<UsuSelFavoritos> getEliminados(List<Usuario> nuevos, Usuario logged, Integer idDependencia) {
        List<UsuSelFavoritos> eliminados = new ArrayList<>();
        for (UsuSelFavoritos favo : logged.getUsuSelFavoritosList()) {
            boolean flag = true;
            for (Usuario usuNu : nuevos) {
                //dependencia a la que pertenece el usuario favorito
                Usuario usuFav = favo.getUsuFav();
                Dependencia dependencia = usuFav.getDependencia();
                if (dependencia.getId().equals(idDependencia)) {
                    if (usuFav.getId().equals(usuNu.getId())) {
                        flag = false;
                        break;
                    }
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag) eliminados.add(favo);
        }
        return eliminados;
    }

    public List<String> findAllActiveRolByLogin(String login) {
        return usuarioRepository.findAllActiveRolByLogin(login);
    }

    /**
     * @param login Nombre de usuario.
     * @date 06/04/2020 hotfix_62 Error autenticacion de usuario.
     * Metodo que permite actualizar los principales datos del usuario con los que tiene en el LDAP.
     */
    @Transactional
    public void actualizarUsuario(String login) {
        Usuario usuarioLdap = ldapService.getUsuarioFromLdapByAccountName(login.toLowerCase());
        if (usuarioLdap != null) {
            Usuario usuario = findByLoginAndActivo(login.toLowerCase(), Boolean.TRUE);
            /*
            2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech)
            feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
            */
            usuario.setUsuGrado(usuarioLdap.getUsuGrado());
            usuario.setNombre(usuarioLdap.getNombre());
            usuario.setDocumento(usuarioLdap.getDocumento());
            usuario.setTelefono(usuarioLdap.getTelefono());
            usuario.setDependencia(usuarioLdap.getDependencia());
            usuario.setEmail(usuarioLdap.getEmail());
            Usuario usu = usuarioRepository.save(usuario);
        }
    }

    public List<Usuario> getUsersByDependencies(List<Dependencia> deps) {
        return usuarioRepository.getUsersByDependencies(deps);
    }

    public List<Usuario> findAllUsersActive() {
        return usuarioRepository.findAllByActivoTrue();
    }

    public List<Usuario> findAllUsers() {
        return usuarioRepository.findAll();
    }
}
