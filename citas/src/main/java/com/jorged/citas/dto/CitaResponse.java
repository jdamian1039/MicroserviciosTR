package com.jorged.citas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jorged.commons.dto.medicos.DatosMedico;
import com.jorged.commons.dto.pacientes.DatosPaciente;

import java.time.LocalDateTime;
import java.util.Objects;

public record CitaResponse(
        Long id,
        DatosPaciente paciente,
        DatosMedico medico,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fechaCita,
        String sintomas,
        String estadoCita
) {
}
