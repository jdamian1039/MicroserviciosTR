package com.jorged.commons.client;

import com.jorged.commons.dto.pacientes.PacienteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "paciente")
public interface PacienteClient {
    @GetMapping("/{id}")
    PacienteResponse obtenerPorId(@PathVariable Long id);

    @GetMapping("/id-paciente/{id}")
    PacienteResponse buscarPacienteSinEstado(@PathVariable Long id);
}
