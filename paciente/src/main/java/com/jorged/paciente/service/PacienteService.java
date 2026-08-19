package com.jorged.paciente.service;

import com.jorged.commons.dto.pacientes.PacienteRequest;
import com.jorged.commons.dto.pacientes.PacienteResponse;
import com.jorged.commons.services.CrudService;

import java.util.List;

public interface PacienteService extends CrudService<PacienteRequest, PacienteResponse> {

    PacienteResponse buscarPacienteSinEstado(Long id);

}
