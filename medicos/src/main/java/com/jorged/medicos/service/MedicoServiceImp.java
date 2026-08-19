package com.jorged.medicos.service;

import com.jorged.commons.dto.medicos.MedicoRequest;
import com.jorged.commons.dto.medicos.MedicoResponse;
import com.jorged.commons.enums.DisponibilidadMedico;
import com.jorged.commons.enums.EspecialidadMedico;
import com.jorged.commons.enums.EstadoRegistro;
import com.jorged.commons.exceptions.RecursoNoEncontradoException;
import com.jorged.medicos.entity.Medico;
import com.jorged.medicos.mapper.MedicoMapper;
import com.jorged.medicos.repository.MedicoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class MedicoServiceImp implements MedicoService{

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponse> listar() {
        log.info("Buscando medicos activos...");
        return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(medicoMapper::entidadResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicoResponse obtenerPorId(Long id) {
        log.info("Buscando al medico {} activo...", id);
        return medicoMapper.entidadResponse(obtenerMedicoActivoOrExcep(id));
    }

    @Override
    @Transactional(readOnly = true)
    public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {
        log.info("Buscando al medico {} sin estado...", id);
        return medicoMapper.entidadResponse(medicoRepository.findById(id).
                orElseThrow(() -> new RecursoNoEncontradoException(
                        "Medico no encontrado con el id" + id)));
    }

    @Override
    public MedicoResponse registrar(MedicoRequest request) {
        log.info("Registrando medico...");
        validarDatosUnicos(request);

        Medico medico = medicoMapper.requestAEntidad(request);
        medico.actualizarEspecialidad(
                EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad())
        );
        medicoRepository.save(medico);
        log.info("Medico {} {} {} registrado", medico.getNombre(), medico.getApellidoPaterno(),
                medico.getApellidoMaterno());
        return medicoMapper.entidadResponse(medico);
    }

    @Override
    public MedicoResponse actualizar(MedicoRequest request, Long id) {
        Medico medico = obtenerMedicoActivoOrExcep(id);

        validarCambiosUnicos(request, id);

        medico.actualizarMedico(request.nombre(), request.apellidoPaterno(), request.apellidoMaterno(), request.edad(),
                request.email(), request.telefono(), request.cedulaProfesional(),
                EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad()));

        log.info("Registro actualizado del Medico {} {} {}", request.nombre(), request.apellidoPaterno(),
                request.apellidoMaterno());
        return medicoMapper.entidadResponse(medico);
    }

    @Override
    public void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {
        Medico medico = obtenerMedicoActivoOrExcep(idMedico);
        log.info("Cambiando disponibilidad de Medico {} a {}...", idMedico, idDisponibilidad);
        DisponibilidadMedico nuevaDisponibilidad = DisponibilidadMedico
                .obtenerDisponibilidadPorCodigo(idDisponibilidad);
        DisponibilidadMedico disponibilidadAnterior = medico.getDisponibilidad();
        medico.actualizarDisponibilidad(nuevaDisponibilidad);
        log.info("Disponibilidad cambiada de {} a {}", disponibilidadAnterior, nuevaDisponibilidad);

    }

    @Override
    public void eliminar(Long id) {
        Medico medico = obtenerMedicoActivoOrExcep(id);

        log.info("Eliminar medico {}", id);

        medico.eliminar();

        log.info("Medico ha sido ELIMINADO");
    }

    private Medico obtenerMedicoActivoOrExcep(Long id){
        log.info("Buscando al id {}", id);
        return medicoRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro al medico activo con id " + id));
    }

    private void validarDatosUnicos(MedicoRequest request){
        log.info("Validar info unica...");
        log.info("email...");
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email().trim(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un medico activo con el email ingresado");
        log.info("telefono...");
        if (medicoRepository.existsByTelefonoAndEstadoRegistro(request.telefono().trim(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un medico activo con el telefono ingresado");
        log.info("cedula...");
        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistro(request.cedulaProfesional().trim(),
                EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un medico activo con la cédula ingresada");
    }

    private void validarCambiosUnicos(MedicoRequest request, Long id){
        log.info("Validar info unica entre doctores ya existentes...");
        log.info("email a actualizar...");
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(request.email().trim(),
                EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un medico activo con el email ingresado");
        log.info("telefono a actualizar...");
        if (medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(request.telefono().trim(),
                EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un medico activo con el telefono ingresado");
        log.info("cedula a actualizar...");
        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistroAndIdNot(
                request.cedulaProfesional().trim(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un medico activo con la cédula ingresada");
    }
}
