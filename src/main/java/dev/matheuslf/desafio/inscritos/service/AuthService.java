package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.login.LoginRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.login.LoginResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        UsernamePasswordAuthenticationToken usernamePassword  = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }
}
