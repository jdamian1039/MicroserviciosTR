package com.jorged.citas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CitaRequest(
        @NotNull(message = "El paciente es requerido")
        @Positive(message = "El identificador del paciente debe ser positivo")
        Long idPaciente,
        @NotNull(message = "El medico es requerido")
        @Positive(message = "El identificador del medico debe ser positivo")
        Long idMedico,
        @NotNull(message = "Fecha requerida")
        @FutureOrPresent(message = "Campo fecha cita debe ser de mañana en adelante")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fechaCita,
        @NotBlank(message = "Sintomas requeridos")
        @Size(min = 20, max = 500, message = "Campo sintomas debe tener de 20-500 caracteres")
        String sintomas
) {
}
