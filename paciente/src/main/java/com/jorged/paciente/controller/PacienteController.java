package com.jorged.paciente.controller;

import com.jorged.commons.controllers.CommonController;
import com.jorged.commons.dto.medicos.MedicoRequest;
import com.jorged.commons.dto.medicos.MedicoResponse;
import com.jorged.commons.dto.pacientes.PacienteRequest;
import com.jorged.commons.dto.pacientes.PacienteResponse;
import com.jorged.paciente.service.PacienteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class PacienteController extends CommonController<PacienteRequest, PacienteResponse, PacienteService> {
    public PacienteController(PacienteService service) {
        super(service);
    }

    @GetMapping("/id-paciente/{id}")
    public ResponseEntity<PacienteResponse> buscarPacienteSinEstado(@PathVariable @Positive(message = "El id debe ser positivo") Long id){
        return ResponseEntity.ok(service.buscarPacienteSinEstado(id));
    }
}
