package com.jorged.citas.service;

import com.jorged.citas.dto.CitaRequest;
import com.jorged.citas.dto.CitaResponse;
import com.jorged.citas.entity.Cita;
import com.jorged.citas.enums.EstadoCita;
import com.jorged.citas.mapper.CitaMapper;
import com.jorged.citas.repository.CitaRepository;
import com.jorged.commons.client.MedicoClient;
import com.jorged.commons.dto.medicos.MedicoResponse;
import com.jorged.commons.enums.EstadoRegistro;
import com.jorged.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitaServiceImp implements CitaService{
    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final MedicoClient medicoClient;

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
        Cita cita = obtenerCitaOrException(idCita);
        cita.actualizarEstadoCita(EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita));

    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        log.info("Listando citas activas...");
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(cita -> citaMapper
                        .entidadResponse(cita, null, obtenerMedicoSinEstado(cita.getId()))).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse obtenerPorId(Long id) {
        Cita cita = obtenerCitaOrException(id);
        return citaMapper.entidadResponse(cita, null, obtenerMedicoSinEstado(cita.getIdMedico()));
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
        Cita cita = citaMapper.requestAEntidad(request);
        citaRepository.save(cita);
        return citaMapper.entidadResponse(cita, null, medico);
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        Cita cita = obtenerCitaOrException(id);
        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        log.info("Actualizando cita con id {}", id);

        cita.actualizar(request.idPaciente(), request.idMedico(), request.fechaCita(), request.sintomas());

        return citaMapper.entidadResponse(cita, null, medico);
    }

    @Override
    public void eliminar(Long id) {
        Cita cita = obtenerCitaOrException(id);
        cita.eliminar();
    }

    private Cita obtenerCitaOrException(Long id){
        log.info("Consultando la cita solicitada...");
        return citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada con id: " + id));
    }

    private MedicoResponse obtenerMedicoActivo(Long id){
        return medicoClient.obtenerMedicoActivoPorId(id);
    }

    private MedicoResponse obtenerMedicoSinEstado(Long id){
        return medicoClient.obtenerMedicoPorIdSinEstado(id);
    }
}
