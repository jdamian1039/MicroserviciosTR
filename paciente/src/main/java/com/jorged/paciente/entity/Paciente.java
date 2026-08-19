package com.jorged.paciente.entity;

import com.jorged.commons.enums.EstadoRegistro;
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

    public void generarIMC(Double estatura, Double peso){
        this.indiceMasaCorp = peso/(estatura * estatura);
    }

    public void generarExpediente(String telefono) {
        if (telefono == null)
            throw new IllegalArgumentException("Telefono vacio");
        this.expediente = telefono.replaceAll("\\d", "$0X");
    }

    public void borradoLogico(){
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    public void actualizarPaciente(String nombre, String apellidoPaterno, String apellidoMaterno,
                    Short edad, Double peso, Double estatura, String email, String telefono,
                                   String direccion) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;

        generarExpediente(telefono);
        generarIMC(estatura, peso);
    }
}
