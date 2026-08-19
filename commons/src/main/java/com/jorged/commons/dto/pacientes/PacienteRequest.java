package com.jorged.commons.dto.pacientes;

import jakarta.validation.constraints.*;


public record PacienteRequest(
        @NotBlank(message = "Es requerido y no puede ir en blanco")
        @Size(min = 1, max = 50, message = "Debe contener de 1 a 50 caracteres")
        String nombre,
        @NotBlank(message = "Es requerido y no puede ir en blanco")
        @Size(min = 1, max = 50, message = "Debe contener de 1 a 50 caracteres")
        String apellidoPaterno,
        @NotBlank(message = "Es requerido y no puede ir en blanco")
        @Size(min = 1, max = 50, message = "Debe contener de 1 a 50 caracteres")
        String apellidoMaterno,
        @NotNull(message = "Es requerido")
        @Min(value = 1, message = "Edad minima de 1 año")
        @Max(value = 100, message = "Edad maxima de 100 años")
        Short edad,
        @NotNull(message = "Es un campo requerido")
        @DecimalMin(value = "0.1", message = "No puede ser menor a 0.1 kg")
        @DecimalMax(value = "200.0", message = "No puede ser mayor a 200 kg")
        Double peso,
        @NotNull(message = "Es un campo requerido")
        @DecimalMin(value = "1.0", message = "No puede ser menor a 1 m")
        @DecimalMax(value = "2.0", message = "No puede ser mayor a 2 m")
        Double estatura,
        @NotBlank(message = "Es requerido y no puede ir en blanco")
        @Size(min = 1, max = 100, message = "Debe contener de 1 a 100 caracteres")
        @Email(message = "Debe cumplir con el formato ejemplo@dominio.com")
        String email,
        @NotBlank(message = "Es requerido y no puede ir en blanco")
        @Pattern(regexp = "^[0-9]{10}$", message = "Debe ser compuesto por 10 digitos")
        String telefono,
        @NotBlank(message = "Es requerido y no puede ir en blanco")
        @Size(min = 1, max = 150, message = "Debe contener de 1 a 150 caracteres")
        String direccion
) {
}
