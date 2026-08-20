package com.jorged.citas.mapper;

import com.jorged.citas.dto.CitaRequest;
import com.jorged.citas.dto.CitaResponse;
import com.jorged.citas.entity.Cita;
import com.jorged.commons.dto.medicos.DatosMedico;
import com.jorged.commons.dto.medicos.MedicoResponse;
import com.jorged.commons.dto.pacientes.DatosPaciente;
import com.jorged.commons.dto.pacientes.PacienteResponse;
import com.jorged.commons.mapper.CommonMapper;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper implements CommonMapper<CitaRequest, CitaResponse, Cita> {
    @Override
    public Cita requestAEntidad(CitaRequest request) {
        if (request == null) return null;
        return Cita.crear(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );
    }

    @Override
    public CitaResponse entidadResponse(Cita entidad) {
        if (entidad == null) return null;
        return new CitaResponse(
                entidad.getId(),
                null,
                null,
                entidad.getFechaCita(),
                entidad.getSintomas(),
                entidad.getEstadoCita().getDescripcion()
        );
    }

    public CitaResponse entidadResponse(Cita entidad, PacienteResponse paciente, MedicoResponse medico) {
        if (entidad == null) return null;
        return new CitaResponse(
                entidad.getId(),
                pacienteResponseADatosPaciente(paciente),
                medicoResponseADatosMedico(medico),
                entidad.getFechaCita(),
                entidad.getSintomas(),
                entidad.getEstadoCita().getDescripcion()
        );
    }
    private DatosPaciente pacienteResponseADatosPaciente(PacienteResponse paciente){
        if (paciente == null) return null;

        return new DatosPaciente(
                paciente.nombre(),
                paciente.numExpediente(),
                paciente.edad() + " años",
                paciente.peso() + " kg.",
                paciente.estatura() + " m.",
                String.join(" ", Math.round(paciente.imc() * 100.0)/100.0 + "",
                        clasificacionIMC(paciente.imc())),
                paciente.telefono()
        );
    }

    private String clasificacionIMC(double imc){
        if (imc < 18.5) return "Bajo peso";
        if (imc < 25) return "Peso normal";
        if (imc < 30) return "Sobrepeso";
        if (imc < 35) return "Obesidad grado I";
        if (imc < 40) return "Obesidad grado II";
        return  "Obesidad grado III";
    }

    private DatosMedico medicoResponseADatosMedico(MedicoResponse medico){
        if (medico == null) return null;
        return new DatosMedico(
                medico.nombre(),
                medico.cedulaProfesional(),
                medico.especialidad()
        );
    }
}
