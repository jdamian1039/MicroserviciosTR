package com.jorged.commons.client;

import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "citas")
public interface CitaClient {

    @GetMapping("/agenda-medico/{idMedico}")
    ResponseEntity<Void> validarAgendaMedico(@PathVariable Long idMedico);

    @GetMapping("/agenda-paciente/{idPaciente}")
    ResponseEntity<Void> validarAgendaPaciente(@PathVariable Long idPaciente);
}
