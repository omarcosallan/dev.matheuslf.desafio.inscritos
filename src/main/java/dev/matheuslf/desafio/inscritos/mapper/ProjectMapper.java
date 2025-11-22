package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectUpdateDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.InvalidDateException;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ProjectResponseDTO toDTO(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                userMapper.toDTO(project.getOwner()),
                project.getAssignees() != null
                        ? project.getAssignees().stream().map(userMapper::toDTO).toList()
                        : List.of()
        );
    }

    public Project toEntity(ProjectRequestDTO dto) {
        User owner = userRepository.findByEmail(dto.ownerEmail())
                .orElseThrow( () -> new ResourceNotFoundException("Owner não encontrado"));
        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        project.setOwner(owner);
        return project;
    }

    public void updateEntity(Project project, ProjectUpdateDTO dto) {
        if (dto.name() != null) {
            project.setName(dto.name());
        }

        if (dto.description() != null) {
            project.setDescription(dto.description());
        }

        if (dto.startDate() != null) {
            if (dto.endDate().isBefore(dto.startDate()) || dto.endDate().isBefore(project.getStartDate())) {
                throw new InvalidDateException("A data de início do projeto deve ser anterior à data de término");
            }
            project.setStartDate(dto.startDate());
        }

        if (dto.endDate() != null) {
            if (dto.endDate().isBefore(dto.startDate()) || dto.endDate().isBefore(project.getStartDate())) {
                throw new InvalidDateException("A data de início do projeto deve ser anterior à data de término");
            }
            project.setEndDate(dto.endDate());
        }

        if (dto.ownerEmail() != null) {
            User owner = userRepository.findByEmail(dto.ownerEmail())
                    .orElseThrow( () -> new ResourceNotFoundException("Owner não encontrado"));
            project.setOwner(owner);
        }
    }
}
