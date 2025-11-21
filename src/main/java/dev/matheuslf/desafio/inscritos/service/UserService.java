package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.user.UserRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.user.UserResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.mapper.UserMapper;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import dev.matheuslf.desafio.inscritos.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final UserValidator userValidator;

    public UserResponseDTO save(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(encoder.encode(user.getPassword()));
        userValidator.validateUserEmail(user);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }
}
