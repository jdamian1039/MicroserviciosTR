package com.jorged.auth.services;

import java.util.Set;

import com.jorged.auth.dto.UsuarioRequest;
import com.jorged.auth.dto.UsuarioResponse;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}
