package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateUserEmail(User user) {
        if (existsRegisteredUser(user)) {
            throw new ConflictException("Usuário já está cadastrado");
        }
    }

    private boolean existsRegisteredUser(User user) {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        if (user.getId() == null) {
            return foundUser.isPresent();
        }

        return foundUser.isPresent() && !user.getId().equals(foundUser.get().getId());
    }
}
