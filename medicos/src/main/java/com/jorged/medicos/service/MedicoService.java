package com.jorged.medicos.service;

import com.jorged.commons.dto.medicos.MedicoRequest;
import com.jorged.commons.dto.medicos.MedicoResponse;
import com.jorged.commons.services.CrudService;

public interface MedicoService extends CrudService<MedicoRequest, MedicoResponse> {
    MedicoResponse obtenerMedicoPorIdSinEstado(Long id);
    void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad);
}
