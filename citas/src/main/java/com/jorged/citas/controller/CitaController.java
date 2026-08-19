package com.jorged.citas.controller;

import com.jorged.citas.dto.CitaRequest;
import com.jorged.citas.dto.CitaResponse;
import com.jorged.citas.service.CitaService;
import com.jorged.commons.controllers.CommonController;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class CitaController extends CommonController<CitaRequest, CitaResponse, CitaService> {
    public CitaController(CitaService service) {
        super(service);
    }
    @PatchMapping("/{idCita}/estado/{idEstado}") //Feign no soporta Patch
    public ResponseEntity<Void> actualizarEstadoCita(
            @PathVariable @Positive(message = "Id Cita debe ser positivo") Long idCita,
            @PathVariable @Positive(message = "Id Estado debe ser positivo") Long idEstado){

        service.actualizarEstadoCita(idCita, idEstado);
        return ResponseEntity.noContent().build();
    }

}
