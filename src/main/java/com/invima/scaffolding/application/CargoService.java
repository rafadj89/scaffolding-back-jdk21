package com.invima.scaffolding.application;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.invima.scaffolding.application.dto.CargoDTO;
import com.invima.scaffolding.domain.model.Cargo;
import com.invima.scaffolding.domain.model.Usuario;
import com.invima.scaffolding.infrastructure.adaptador.CargosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author egonzalezm
 */
@Service
public class CargoService {

    private static final Logger LOG = LoggerFactory.getLogger(CargoService.class);

    @Autowired
    CargosRepository cargosRepository;

    public Cargo findById(final int id) {
        return cargosRepository.findById(id).orElse(null);
    }

    public List<Cargo> findAll() {
        return cargosRepository.findAllByOrderByCarNombre();
    }


    @Autowired
    private DataSource dataSource;


    public String guardarCargo(Cargo cargo) {
        String mensaje = "OK";
        try {
            if (cargo.getCarNombre() == null || cargo.getCarNombre().trim().length() == 0) {
                mensaje = "Error-El nombre del cargo es obligatorio.";
                return mensaje;
            }

            if (cargo.getId() != null) {
                int numRegistros = cargosRepository.findregistrosNombreRepetido(cargo.getCarNombre(), cargo.getId());
                if (numRegistros > 0) {
                    mensaje = "Error-El nombre del cargo ya se encuentra registrado en el sistema.";
                    return mensaje;
                }
            }
            cargo.setCarIndLdap(Boolean.FALSE);
            cargosRepository.save(cargo);
        } catch (Exception ex) {
            LOG.error(null, ex);
            mensaje = "Excepcion-" + ex.getMessage();
        }
        return mensaje;
    }

    /**
     * Construye una lista de DTO de cargos correspondientes a un usuario, según
     * su orden de prioridad, para utilizar en las interfaces gráficas.
     *
     * @param usuario Usuario.
     * @return Lista de DTO de cargos correspondientes al usuario.
     */
    /*
     * 2018-05-17 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Utilización de la lógica de construcción de los DTO para
     * utilizar en diferentes UI.
     */
    public List<CargoDTO> buildCargosXUsuario(final Usuario usuario) {
        final List<Object[]> list = cargosRepository.findCargosXusuario(usuario.getId());
        final List<CargoDTO> cargos = new ArrayList<>();
        for (Object[] data : list) {
            cargos.add(new CargoDTO(((BigDecimal) data[0]).intValue(), (String) data[1]));
        }
        return cargos;
    }

    /**
     * Obtiene una lista de IDs y nombres de los cargos asociados a un usuario.
     *
     * @param usuID ID del usuario.
     * @return Lista de IDs y nombres de los cargos asociados al usuario.
     */
    /*
     * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public List<Object[]> findCargosXusuario(Integer usuID) {
        return cargosRepository.findCargosXusuario(usuID);
    }

    /**
     * Busca un cargo por su ID.
     *
     * @param id ID del cargo.
     * @return Instancia del cargo del ID, o {@code null} en caso que no exista
     * correspondencia en el sistema.
     */
    public Cargo findOne(Integer id) {
        //return cargosRepository.findOne(id);
        //Actualizacion Spring 2
        return cargosRepository.findById(id).get();
    }

    public Cargo findCargoParaUsuario(final Integer id, final Usuario usuario) {
        // colocar bloque if para ver si entra nulo el usuario si sigue saliendo el error
        if (usuario.getUsuCargoPrincipalId().getId().equals(id) ||
                usuario.getUsuCargo1Id().getId().equals(id) ||
                usuario.getUsuCargo2Id().getId().equals(id) ||
                usuario.getUsuCargo3Id().getId().equals(id) ||
                usuario.getUsuCargo4Id().getId().equals(id) ||
                usuario.getUsuCargo5Id().getId().equals(id) ||
                usuario.getUsuCargo6Id().getId().equals(id) ||
                usuario.getUsuCargo7Id().getId().equals(id) ||
                usuario.getUsuCargo8Id().getId().equals(id) ||
                usuario.getUsuCargo9Id().getId().equals(id) ||
                usuario.getUsuCargo10Id().getId().equals(id))
            return findOne(id);
        return null;
    }


    @Deprecated
    public List<CargoDTO> retornaConsultaDTO(String criteria, int inicio, int fin) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String select = "SELECT CAR_ID , COUNT(*) OVER () RESULT_COUNT, CAR_NOMBRE ,ROWNUM CNUM   FROM CARGO";

        if(criteria != null){
            select +=  "  WHERE  LOWER(CAR_NOMBRE)  LIKE '%" + criteria.trim().toLowerCase() + "%'";
        }

        String sql = "select c.CAR_ID , c.RESULT_COUNT, c.CAR_NOMBRE \n" +
                "from \n" +
                "(\n" +
                "\t" +  select + " \n" +
                ") c\n" +
                "where c.CNUM \n" +
                "between ? and ?\n" +
                "ORDER BY c.CAR_NOMBRE  ASC ";

        LinkedList<Object> parameters = new LinkedList<>();

        parameters.add(inicio);
        parameters.add(fin);

        return jdbcTemplate.query(sql, parameters.toArray(), new RowMapper<CargoDTO>() {
            @Override
            public CargoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                CargoDTO c = new CargoDTO();

                c.setId(rs.getInt("CAR_ID"));
                c.setNombre(rs.getString("CAR_NOMBRE"));
                c.setRESULT_COUNT(rs.getInt("RESULT_COUNT"));
                return c;
            }
        });
    }

}
