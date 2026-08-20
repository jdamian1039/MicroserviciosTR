package com.jorged.citas.repository;

import com.jorged.citas.entity.Cita;
import com.jorged.citas.enums.EstadoCita;
import com.jorged.commons.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);
    Optional<Cita> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    @Query(nativeQuery=true, value= """
        SELECT COUNT(ID_CITA) > 0 FROM CITAS WHERE ID_PACIENTE=:idPaciente AND 
            (ESTADO_CITA='PENDIENTE' OR ESTADO_CITA='CONFIRMADA')
    """ )
    Boolean consultarCitasConfirmadasPendientesPaciente(@Param("idPaciente") Long idPaciente);

    @Query(nativeQuery=true, value= """
        SELECT COUNT(ID_CITA) > 0 FROM CITAS WHERE ID_MEDICO=:idMedico AND 
            (ESTADO_CITA='PENDIENTE' OR ESTADO_CITA='CONFIRMADA')
    """ )
    Boolean consultarCitasConfirmadasPendientesMedico(@Param("idMedico") Long idMedico);

    @Query(nativeQuery=true, value= """
        SELECT COUNT(ID_CITA) FROM CITAS WHERE ID_CITA=:idCita AND 
            ESTADO_CITA NOT IN ('PENDIENTE', 'CONFIRMADA', 'CANCELADA')
    """ )
    Integer consultarCitasParaEliminar(@Param("idCita") Long idCita);

    //boolean existsByIdPacienteAndEstadoCitaIn(Long idPaciente, Collection<EstadoCita> estadoCitas);
    //boolean existsByIdPacienteAndEstadoCita(Long idPaciente, EstadoCita estadoCita);
}
