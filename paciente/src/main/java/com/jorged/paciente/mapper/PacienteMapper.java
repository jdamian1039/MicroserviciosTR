package com.jorged.paciente.mapper;

import com.jorged.commons.dto.pacientes.PacienteRequest;
import com.jorged.commons.dto.pacientes.PacienteResponse;
import com.jorged.commons.mapper.CommonMapper;
import com.jorged.paciente.entity.Paciente;
import com.jorged.commons.enums.EstadoRegistro;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper implements CommonMapper<PacienteRequest, PacienteResponse, Paciente> {

    public Paciente requestAEntidad(PacienteRequest request){
        if (request==null) return null;
        Paciente paciente = Paciente.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .direccion(request.direccion())
                .edad(request.edad())
                .peso(request.peso())
                .email(request.email())
                .telefono(request.telefono())
                .estatura(request.estatura())
                .estadoRegistro(EstadoRegistro.ACTIVO).build();

        paciente.generarExpediente(request.telefono());
        paciente.generarIMC(request.estatura(), request.peso());

        return paciente;
    }

    public PacienteResponse entidadResponse(Paciente entidad){
        if (entidad==null) return null;

        return new PacienteResponse(
                entidad.getId(),
                String.join(" ", entidad.getNombre(),
                        entidad.getApellidoPaterno(), entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getPeso(),
                entidad.getEstatura(),
                entidad.getIndiceMasaCorp(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getDireccion(),
                entidad.getExpediente()
        );
    }
}
