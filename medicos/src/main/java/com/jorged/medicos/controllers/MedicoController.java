package com.jorged.medicos.controllers;

import com.jorged.commons.controllers.CommonController;
import com.jorged.commons.dto.medicos.MedicoRequest;
import com.jorged.commons.dto.medicos.MedicoResponse;
import com.jorged.medicos.service.MedicoService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class MedicoController extends CommonController<MedicoRequest, MedicoResponse, MedicoService> {
    public MedicoController(MedicoService service) {
        super(service);
    }

    @GetMapping("/id-medico/{id}")
    public ResponseEntity<MedicoResponse> obtenerMedicoPorIdSinEstado(
            @PathVariable @Positive(message = "Id debe ser valor positivo") Long id
    ){
        return  ResponseEntity.ok(service.obtenerMedicoPorIdSinEstado(id));
    }
    @PutMapping("/{idMedico}/disponibilidad/{idDisponibilidad}")
    public ResponseEntity<MedicoResponse> obtenerMedicoPorIdSinEstado(
            @PathVariable @Positive(message = "Id medico debe ser valor positivo") Long idMedico,
            @PathVariable @Positive(message = "Id disp. debe ser valor positivo") Long idDisponibilidad
    ){
        service.actualizarDisponibilidadMedico(idMedico, idDisponibilidad);
        return  ResponseEntity.noContent().build();
    }

}
