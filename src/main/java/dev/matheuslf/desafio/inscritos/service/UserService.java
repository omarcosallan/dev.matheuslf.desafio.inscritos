package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.user.UserRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.user.UserResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.mapper.UserMapper;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    public UserResponseDTO save(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(encoder.encode(user.getPassword()));

        if (existsRegisteredUser(user)) {
            throw new ConflictException("There is already a user with this email");
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with email: " + email));
    }

    private boolean existsRegisteredUser(User user) {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        if (user.getId() == null) {
            return foundUser.isPresent();
        }

        return foundUser.isPresent() && !user.getId().equals(foundUser.get().getId());
    }
}
