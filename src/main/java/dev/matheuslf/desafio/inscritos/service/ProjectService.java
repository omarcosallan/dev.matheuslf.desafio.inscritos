package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import dev.matheuslf.desafio.inscritos.validator.ProjectValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final String PROJECT_NOT_FOUND_MESSAGE = "Projeto não encontrado";
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectValidator projectValidator;
    private final UserRepository userRepository;

    public ProjectResponseDTO save(ProjectRequestDTO dto) {
        Project project = projectMapper.toEntity(dto);
        projectValidator.validate(project);

        if (dto.endDate().isBefore(project.getStartDate())) {
            throw new ConflictException("A data de início do projeto deve ser anterior à data de término");
        }

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }

    public PageResponse<ProjectResponseDTO> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projects = projectRepository.findAll(pageable);
        PageResponse<ProjectResponseDTO> response = new PageResponse<>(
                projects.getContent().stream().map(projectMapper::toDTO).toList(),
                projects.getNumber(),
                projects.getTotalPages(),
                projects.getTotalElements(),
                size,
                projects.hasNext(),
                projects.hasPrevious()
        );
        return response;
    }

    public List<ProjectResponseDTO> findByOwner(UUID ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner não encontrado"));
        return projectRepository.findByOwner(owner).stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO update(UUID id, @Valid UpdateProjectDTO dto) {
        Project project = getById(id);
        projectValidator.validate(project);

        if (project.getStartDate().isBefore(LocalDate.now())) {
            throw new ConflictException("Não é possível atualizar um projeto já iniciado");
        }

        if (project.getEndDate().isBefore(dto.startDate()) || dto.endDate().isBefore(project.getStartDate())) {
            throw new ConflictException("A data de início do projeto deve ser anterior à data de término");
        }

        projectMapper.updateEntity(project, dto);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDTO(updatedProject);
    }

    private Project getById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE));
    }
}
