package com.jorged.citas.service;

import com.jorged.citas.dto.CitaRequest;
import com.jorged.citas.dto.CitaResponse;
import com.jorged.citas.entity.Cita;
import com.jorged.citas.enums.EstadoCita;
import com.jorged.citas.mapper.CitaMapper;
import com.jorged.citas.repository.CitaRepository;
import com.jorged.commons.client.MedicoClient;
import com.jorged.commons.client.PacienteClient;
import com.jorged.commons.dto.medicos.MedicoResponse;
import com.jorged.commons.dto.pacientes.PacienteResponse;
import com.jorged.commons.enums.DisponibilidadMedico;
import com.jorged.commons.enums.EstadoRegistro;
import com.jorged.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitaServiceImp implements CitaService{
    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final MedicoClient medicoClient;
    private final PacienteClient pacienteClient;

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
        Cita cita = obtenerCitaOrException(idCita);
        cita.actualizarEstadoCita(EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita));

    }

    @Override
    public void validarAgendaMedico(Long idMedico) {
        validarCitasMedico(idMedico);
    }

    @Override
    public void validarAgendaPaciente(Long idPaciente) {
        validarCitasPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        log.info("Listando citas activas...");
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(cita -> citaMapper
                        .entidadResponse(cita, obtenerPacienteSinEstado(cita.getIdPaciente()),
                                obtenerMedicoSinEstado(cita.getIdMedico()))).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse obtenerPorId(Long id) {
        Cita cita = obtenerCitaOrException(id);
        return citaMapper.entidadResponse(cita, obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico()));
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
        validarEstatusMedico(request.idMedico());
        PacienteResponse paciente = obtenerPacienteActivo(request.idPaciente());
        validarCitasPaciente(request.idPaciente());
        Cita cita = citaMapper.requestAEntidad(request);

        citaRepository.save(cita);
        medicoClient.actualizarDisponibilidadMedico(request.idMedico(),
                DisponibilidadMedico.NO_DISPONIBLE.getCodigo());

        return citaMapper.entidadResponse(cita, paciente, medico);
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        Cita cita = obtenerCitaOrException(id);
        validarCitaActual(cita.getEstadoCita().getCodigo());
        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
        if (!Objects.equals(medico.id(), cita.getIdMedico()))
            validarEstatusMedico(request.idMedico());

        PacienteResponse paciente = obtenerPacienteActivo(request.idPaciente());
        validarCitasPaciente(request.idPaciente());

        log.info("Actualizando cita con id {}", id);
        cita.actualizar(request.idPaciente(), request.idMedico(), request.fechaCita(), request.sintomas());
        if (Objects.equals(medico.id(), cita.getIdMedico()))
            medicoClient.actualizarDisponibilidadMedico(request.idMedico(),
                    DisponibilidadMedico.DISPONIBLE.getCodigo());

        return citaMapper.entidadResponse(cita, paciente, medico);
    }

    @Override
    public void eliminar(Long id) {
        Cita cita = obtenerCitaOrException(id);
        if (citaRepository.consultarCitasParaEliminar(id) > 0)
            throw new IllegalArgumentException("La cita solo puede estar PENDIENTE, " +
                    "CANCELADA o FINALIZADA para ser eliminada");
        log.info(cita.getEstadoCita().getDescripcion());
        cita.eliminar();

        medicoClient.actualizarDisponibilidadMedico(cita.getIdMedico(),
                DisponibilidadMedico.DISPONIBLE.getCodigo());
    }

    private Cita obtenerCitaOrException(Long id){
        log.info("Consultando la cita solicitada...");
        return citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada con id: " + id));
    }

    private PacienteResponse obtenerPacienteActivo(Long id){
        return pacienteClient.obtenerPorId(id);
    }

    private PacienteResponse obtenerPacienteSinEstado(Long id){
        return pacienteClient.buscarPacienteSinEstado(id);
    }

    private MedicoResponse obtenerMedicoActivo(Long id){
        return medicoClient.obtenerMedicoActivoPorId(id);
    }

    private MedicoResponse obtenerMedicoSinEstado(Long id){
        return medicoClient.obtenerMedicoPorIdSinEstado(id);
    }

    private void validarEstatusMedico(Long id){
        DisponibilidadMedico disponible = DisponibilidadMedico
                .obtenerDisponibilidadPorCodigo(id);

        if (disponible != DisponibilidadMedico.DISPONIBLE)
            throw new IllegalArgumentException("No se puede asignar nuevo médico. " +
                    "Su estatus es: " + disponible.getDescripcion());
    }

    private void validarCitasPaciente(Long id){
        if (citaRepository.consultarCitasConfirmadasPendientesPaciente(id))
            throw new IllegalStateException("Paciente ya cuenta con citas agendadas");
    }

    private void validarCitasMedico(Long id){
        if (citaRepository.consultarCitasConfirmadasPendientesMedico(id))
            throw new IllegalStateException("Paciente ya cuenta con citas agendadas");
    }

    private void validarCitaActual(Long id){
        EstadoCita estado = EstadoCita.obtenerEstadoCitaPorCodigo(id);

        if (estado != EstadoCita.CONFIRMADA || estado != EstadoCita.PENDIENTE)
            throw new IllegalArgumentException("Solo se pueden reasignar medico o " +
                    "pacientes a citas CONFIRMADAS o PENDIENTES");
    }

}
