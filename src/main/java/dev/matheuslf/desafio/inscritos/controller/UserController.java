package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.user.UserRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.user.UserResponseDTO;
import dev.matheuslf.desafio.inscritos.service.UserService;
import dev.matheuslf.desafio.inscritos.utils.GenerateURI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController  {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO user = userService.save(dto);
        URI uri = GenerateURI.generate(user.id());
        return ResponseEntity.created(uri).body(user);
    }
}
