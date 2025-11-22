package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.InvalidDateException;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectValidator {

    private final ProjectRepository projectRepository;

    public void validateProjectName(Project project) {
        if (existsRegisteredProject(project)) {
            throw new ConflictException("Já existe um projeto com esse nome");
        }
    }

    public void validateUpdateDateProject(Project project, UpdateProjectDTO dto) {
        if (project.getStartDate().isBefore(LocalDate.now())) {
            throw new InvalidDateException("Não é possível atualizar um projeto já iniciado");
        }

        if (project.getEndDate().isBefore(dto.startDate()) || dto.endDate().isBefore(project.getStartDate())) {
            throw new InvalidDateException("A data de início do projeto deve ser anterior à data de término");
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
