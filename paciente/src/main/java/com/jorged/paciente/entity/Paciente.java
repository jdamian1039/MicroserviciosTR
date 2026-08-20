package com.jorged.paciente.entity;

import com.jorged.commons.enums.EstadoRegistro;
import com.jorged.commons.utils.StringCustomUtils;
import com.jorged.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "PACIENTES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long Id;
    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;
    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apellidoPaterno;
    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apellidoMaterno;
    @Column(name = "EDAD", nullable = false)
    private Short edad;
    @Column(name = "PESO", nullable = false)
    private Double peso;
    @Column(name = "ESTATURA", nullable = false)
    private Double estatura;
    @Column(name = "IMC", nullable = false)
    private Double indiceMasaCorp;
    @Column(name = "EMAIL", length = 100, nullable = false)
    private String email;
    @Column(name = "NUM_EXPEDIENTE", length = 20, nullable = false)
    private String expediente;
    @Column(name = "TELEFONO", length = 10, nullable = false)
    private String telefono;
    @Column(name = "DIRECCION", length = 150, nullable = false)
    private String direccion;
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoRegistro estadoRegistro;

    public void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad, Double peso,
                    Double estatura, String email, String telefono, String direccion) {
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
        StringCustomUtils.validarTamanio(direccion, 1, 150,
                "La cedula es requerido. Debe contener 12 caracteres");
        ValoresNumericosUtils.validarRangoShort(edad, (short)1, (short)100,
                "La edad es requerida y debe ser de 1-100 años");
        ValoresNumericosUtils.validarRangoDouble(estatura, 1.0, 2.0,
                "Estatura requerida y en el rango de 1.0 m a 2.0 m");
        ValoresNumericosUtils.validarRangoDouble(peso, 0.1, 200.0,
                "Peso requerido y en el rango de 0.1 kg a 200.0 kg");
    }

    private void validarNoEliminado(){
        if (this.estadoRegistro==EstadoRegistro.ELIMINADO)
            throw new IllegalArgumentException("El paciente ya esta eliminado");
    }
    public void generarIMC(Double estatura, Double peso){
        this.indiceMasaCorp = peso/(estatura * estatura);
    }

    public void generarExpediente(String telefono) {
        if (telefono == null)
            throw new IllegalArgumentException("Telefono vacio");
        this.expediente = telefono.replaceAll("\\d", "$0X");
    }

    public void borradoLogico(){
        validarNoEliminado();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    public void actualizarPaciente(String nombre, String apellidoPaterno, String apellidoMaterno,
                    Short edad, Double peso, Double estatura, String email, String telefono,
                                   String direccion) {
        validarNoEliminado();
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, edad, peso, estatura,
                email, telefono, direccion);

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.email = email.trim().toLowerCase();
        this.telefono = telefono.trim();
        this.direccion = direccion.trim();

        generarExpediente(telefono);
        generarIMC(estatura, peso);
    }
}
