package com.jorged.medicos.entity;

import com.jorged.commons.enums.DisponibilidadMedico;
import com.jorged.commons.enums.EspecialidadMedico;
import com.jorged.commons.enums.EstadoRegistro;
import com.jorged.commons.utils.StringCustomUtils;
import com.jorged.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEDICOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICO")
    private Long id;
    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;
    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apellidoPaterno;
    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apellidoMaterno;
    @Column(name = "EDAD", nullable = false)
    private Short edad;
    @Column(name = "EMAIL", length = 100, nullable = false)
    private String email;
    @Column(name = "TELEFONO", length = 10, nullable = false)
    private String telefono;
    @Column(name = "CEDULA_PROFESIONAL", length = 12, nullable = false)
    private String cedulaProfesional;
    @Enumerated(EnumType.STRING)
    @Column(name = "ESPECIALIDAD", nullable = false)
    private EspecialidadMedico especialidad;
    @Enumerated(EnumType.STRING)
    @Column(name = "DISPONIBILIDAD", nullable = false)
    private DisponibilidadMedico disponibilidad;
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;

    public void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad, String email,
                  String telefono, String cedulaProfesional, EspecialidadMedico especialidad) {
        StringCustomUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido. Debe contener 1-50 caracteres");
        StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50,
                "El nombre es requerido. Debe contener 1-50 caracteres");
        StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50,
                "El nombre es requerido. Debe contener 1-50 caracteres");
        StringCustomUtils.validarTamanio(email, 1, 100,
                "El nombre es requerido. Debe contener 1-100 caracteres");
        StringCustomUtils.validarTamanio(telefono, 10, 10,
                "El telefono es requerido. Debe contener 10 digitos");
        StringCustomUtils.validarTamanio(cedulaProfesional, 12, 12,
                "La cedula es requerido. Debe contener 12 caracteres");
        ValoresNumericosUtils.validarRangoShort(edad, (short)18, (short)100,
                "La edad es requerida y debe ser de 18-100 años");

        if (especialidad==null)
            throw new IllegalArgumentException("La especialidad es requerida");
    }

    private void validarNoEliminado(){
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalStateException("El medico ya esta eliminado");
    }

    public void actualizarEspecialidad(EspecialidadMedico especialidad){
        validarNoEliminado();
        if (especialidad == null)
            throw new IllegalArgumentException("Especialidad es requerida");
        this.especialidad = especialidad;
    }
    public void actualizarDisponibilidad(DisponibilidadMedico disponibilidad){
        validarNoEliminado();
        if (disponibilidad == null)
            throw new IllegalArgumentException("Disponibilidad es requerida");
        this.disponibilidad = disponibilidad;
    }

    public void eliminar(){
        validarNoEliminado();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    public void actualizarMedico(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad, String email,
                  String telefono, String cedulaProfesional, EspecialidadMedico especialidad) {
        validarNoEliminado();
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, edad, email, telefono, cedulaProfesional,
                especialidad);
        actualizarEspecialidad(especialidad);

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.edad = edad;
        this.email = email.trim().toLowerCase();
        this.telefono = telefono.trim();
        this.cedulaProfesional = cedulaProfesional.trim();
    }
}
