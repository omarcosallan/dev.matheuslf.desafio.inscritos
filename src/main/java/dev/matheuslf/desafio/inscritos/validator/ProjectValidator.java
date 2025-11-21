package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectValidator {

    private final ProjectRepository projectRepository;

    public void validate(Project project) {
        if (existsRegisteredProject(project)) {
            throw new ConflictException("Já existe um projeto com esse nome");
        }
    }

    private boolean existsRegisteredProject(Project project) {
        Optional<Project> foundProject = projectRepository.findByName(project.getName());

        if (project.getId() == null) {
            return foundProject.isPresent();
        }

        return foundProject.isPresent() && !project.getId().equals(foundProject.get().getId());
    }
}
