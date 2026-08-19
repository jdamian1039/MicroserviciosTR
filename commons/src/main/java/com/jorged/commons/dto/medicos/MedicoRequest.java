package com.jorged.commons.dto.medicos;

import jakarta.validation.constraints.*;

public record MedicoRequest(
        @NotBlank(message = "Es requerido")
        @Size(min=1, max=50, message = "Debe contener entre 1 y 50 caracteres")
        String nombre,
        @NotBlank(message = "Es requerido")
        @Size(min=1, max=50, message = "Debe contener entre 1 y 50 caracteres")
        String apellidoPaterno,
        @NotBlank(message = "Es requerido")
        @Size(min=1, max=50, message = "Debe contener entre 1 y 50 caracteres")
        String apellidoMaterno,
        @NotNull(message = "Es requerido")
        @Min(value = 18, message = "La edad minima es de 18 años")
        @Max(value = 100, message = "La edad maxima es de 100 años")
        Short edad,
        @NotBlank(message = "Es requerido")
        @Size(min=1, max=100, message = "Debe contener entre 1 y 100 caracteres")
        @Email(message = "El email debe tener formato ejemplo@dominio.com")
        String email,
        @NotBlank(message = "Es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El telefono solo debe ser 10 digitos")
        String telefono,
        @NotBlank(message = "Es requerido")
        @Size(min=12, max=12, message = "La cedula solo deben ser 12 caracteres")
        String cedulaProfesional,
        @NotNull(message = "Es requerido")
        @Positive(message = "Id de Especialidad debe ser positivo")
        Long idEspecialidad
) {
}
