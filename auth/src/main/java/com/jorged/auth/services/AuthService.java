package com.jorged.auth.services;

import com.jorged.auth.dto.LoginRequest;
import com.jorged.auth.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}

