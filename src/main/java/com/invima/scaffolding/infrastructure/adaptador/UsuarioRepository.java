package com.invima.scaffolding.infrastructure.adaptador;

import java.util.List;

import com.invima.scaffolding.domain.model.Dependencia;
import com.invima.scaffolding.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Obtiene el usuario activo correspondiente al login.
     *
     * @param login Login.
     * @return Usuario activo o {@code null} en caso que no exista un usuario
     * activo correspondiente.
     */
    /*
     * 2017-09-12 jgarcia@controltechcg.com Issue #123 (SICDI-Controltech)
     * hotfix-123: Corrección en búsqueda de usuarios para que únicamente
     * presente información de usuarios activos.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Usuario getByLoginAndActivoTrue(String login);

    @Query(nativeQuery = true, value = "select pr.rol_id from perfil_rol pr join usuario u on u.PER_ID = pr.PER_ID where lower(u.USU_LOGIN) = ?")
    List<String> allByLogin(String login);

    Usuario findByLoginAndActivo(String login, Boolean activo);

    List<Usuario> findByLogin(String login);

    Usuario findByNombre(String nombre);

    @Query(nativeQuery = true, value = "SELECT u.USU_LOGIN, vb.CUANDO, u.USU_NOMBRE, u.USU_GRADO FROM USUARIO_HISTORIAL_FIRMA_IMG vb INNER JOIN USUARIO u ON vb.QUIEN_MOD = u.USU_ID WHERE vb.USU_ID = ? ORDER BY vb.CUANDO DESC")
    List<Object[]> findHistorialFirmaUsuario(Integer id);

    /**
     * Obtiene una lista de los ID de los roles activos asignados a un usuario.
     *
     * @param login Login de usuario.
     * @return Lista de los ID de los roles activos.
     */
    /*
     * 2017-06-22 jgarcia@controltechcg.com Issue #111 (SICDI-Controltech)
     * hotfix-111: Implementación de función que obtiene la lista de roles
     * activos asignados a un usuario, según el perfil asociado.
     */
    @Query(nativeQuery = true, value = ""
            + " SELECT DISTINCT                                                                  "
            + " ROL.ROL_ID                                                                       "
            + " FROM                                                                             "
            + " USUARIO                                                                          "
            + " JOIN PERFIL ON (PERFIL.PER_ID = USUARIO.PER_ID)                                  "
            + " JOIN PERFIL_ROL ON (PERFIL_ROL.PER_ID = PERFIL.PER_ID)                           "
            + " JOIN ROL ON (ROL.ROL_ID = PERFIL_ROL.ROL_ID)                                     "
            + " WHERE                                                                            "
            + " USUARIO.USU_LOGIN = :login                                                       "
            + " AND PERFIL.ACTIVO = 1                                                            "
            + " AND PERFIL_ROL.ACTIVO = 1                                                        "
            + " AND ROL.ACTIVO = 1                                                               "
            + " ORDER BY ROL.ROL_ID                                                              ")
    List<String> findAllActiveRolByLogin(@Param("login") String login);

    /**
     * Busca un usuario activo por el número de documento.
     *
     * @param documento Número de documento.
     * @return Usuario activo o {@code null} si no hay concordancia en el
     * sistema.
     */
    /*
     * 2017-08-30 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech)
     * feature-120: Funciones para buscador de usuarios.
     */
    Usuario findByActivoTrueAndDocumento(String documento);

    /**
     * Obtiene la lista de todos los usuarios ordenados por el peso del grado.
     *
     * @return
     */
    /*
     * 2017-10-05 edison.gonzalez@controltechcg.com Issue #131: Ajuste de orden
     * segun el peso de los grados.
     *
     * 2018-06-06 jgarcia@controltechcg.com Issue #174 (SICDI-Controltech)
     * feature-174: Adición de ordenamiento por nombre.
     */
    @Query(value = "select t from Usuario t order by t.usuGrado.pesoOrden DESC, t.nombre ASC")
    List<Usuario> findAllOrderByGradoDesc();
    
    /**
     * Obtiene la lista de los usuarios por un nombre, paginados
     * @param pNombre nombre para el filtro
     * @param pageable paginador
     * @return lista
     */
        /**
     * 2018-09-20 samuel.delgado@controltechcg.com Issue #174 (SICDI-Controltech)
     * feature-174: Adición para la paginación.
     */
    @Query(value = "select t from Usuario t where (LOWER(t.nombre) like %:pNombre% or"
            + " LOWER(t.dependencia.nombre) like %:pNombre% or LOWER(t.login) like %:pNombre%)"
            + " order by t.usuGrado.pesoOrden DESC, t.nombre ASC")
    Page<Usuario> findAllOrderByNombreContainingGradoDesc(@Param("pNombre") String pNombre, Pageable pageable);

    /**
     * Obtiene la lista de los usuarios activos ordenados por el peso del grado.
     *
     * @return
     */
    /*
     * 2017-10-05 edison.gonzalez@controltechcg.com Issue #131: Ajuste de orden
     * segun el peso de los grados.
     * 
     * 2018-06-06 jgarcia@controltechcg.com Issue #174 (SICDI-Controltech)
     * feature-174: Adición de ordenamiento por nombre.
     */
    @Query(value = "select t from Usuario t where t.activo = 1 order by t.usuGrado.pesoOrden DESC, t.nombre ASC")
    List<Usuario> findAllByActivoTrueOrderByGradoDesc();
    
    /**
     * Obtiene la lista de los usuarios activos por un nombre, paginados
     * @param pNombre nombre para el filtro
     * @param pageable paginador
     * @return lista
     */
    /**
     * 2018-09-20 samuel.delgado@controltechcg.com Issue #174 (SICDI-Controltech)
     * feature-174: Adición para la paginación.
     */
    @Query(value = "select t from Usuario t where t.activo = 1 and (LOWER(t.nombre) like %:pNombre% or"
            + " LOWER(t.dependencia.nombre) like %:pNombre% or LOWER(t.login) like %:pNombre%) "
            + "order by t.usuGrado.pesoOrden DESC, t.nombre ASC")
    Page<Usuario> findAllByActivoTrueAndNombreContainingOrderByGradoDesc(@Param("pNombre") String pNombre, Pageable pageable);
    
    
    /**
     * Obtiene los usuarios activos que pertenecen a la dependencia ordenados
     * por el peso del grado, segun la dependencia.
     *
     * @param dep
     * @return
     */
    /*
     * 2017-10-05 edison.gonzalez@controltechcg.com Issue #131: Ajuste de orden
     * segun el peso de los grados.
     * 
     * 2018-06-06 jgarcia@controltechcg.com Issue #174 (SICDI-Controltech)
     * feature-174: Adición de ordenamiento por nombre.
     */
    @Query(value = "select t from Usuario t where t.activo = 1 and t.usuActivo = 1 and t.dependencia.id = :depId order by t.usuGrado.pesoOrden DESC, t.nombre ASC")
    List<Usuario> findByDependenciaAndActivoTrueOrderByGradoDesc(@Param(value = "depId") Integer dep);
    
    /**
     * Bbtiene las veces que el usuario se ha inactivado en una semana.
     * @param usuId Identificador del usuario.
     * @return numero de veces que se ha inactivado
     */
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM H_USUARIO_ACTIVO WHERE to_number(to_char(CUANDO,'WW')) = to_number(to_char(SYSDATE,'WW')) AND USU_ID = :usuId AND USU_ACTIVO = 0")
    Integer getCountUsuarioCambiosActivoSemana(@Param(value = "usuId") Integer usuId);

    @Query(value = "from Usuario usuario where usuario.dependencia in :depsId and usuario.usuActivo = 1")
    List<Usuario> getUsersByDependencies(@Param("depsId") List<Dependencia> list);

    List<Usuario> findAllByActivoTrue();

    @Query(value = "select u.login from Usuario u where u.activo = 1")
    List<String> getAllLogins();

    Usuario findByIdAndActivoTrue(Integer idUsuario);
}
