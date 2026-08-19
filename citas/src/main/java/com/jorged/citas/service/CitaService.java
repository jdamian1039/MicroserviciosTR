package com.jorged.citas.service;

import com.jorged.citas.dto.CitaRequest;
import com.jorged.citas.dto.CitaResponse;
import com.jorged.commons.services.CrudService;

public interface CitaService extends CrudService<CitaRequest, CitaResponse> {
    void actualizarEstadoCita(Long idCita, Long idEstadoCita);
}
