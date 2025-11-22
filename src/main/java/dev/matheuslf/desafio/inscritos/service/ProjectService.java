package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.enums.Role;
import dev.matheuslf.desafio.inscritos.exception.InvalidDateException;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.exception.UnauthorizedException;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import dev.matheuslf.desafio.inscritos.validator.ProjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        projectValidator.validateProjectName(project);

        if (dto.endDate().isBefore(project.getStartDate())) {
            throw new InvalidDateException("A data de início do projeto deve ser anterior à data de término");
        }

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }

    public PageResponse<ProjectResponseDTO> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projects = projectRepository.findAll(pageable);
        return new PageResponse<>(
                projects.getContent().stream().map(projectMapper::toDTO).toList(),
                projects.getNumber(),
                projects.getTotalPages(),
                projects.getTotalElements(),
                size,
                projects.hasNext(),
                projects.hasPrevious()
        );
    }

    public List<ProjectResponseDTO> findByOwner(UUID ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner não encontrado"));
        return projectRepository.findByOwner(owner).stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO update(User user, UUID id, UpdateProjectDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE));

        if (!isOwnerOrAdmin(user, project)) {
            throw new UnauthorizedException("Você não tem permissão para atualizar esse projeto");
        }

        projectValidator.validateProjectName(project);

        projectMapper.updateEntity(project, dto);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDTO(updatedProject);
    }

    public ProjectResponseDTO findById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE));
        return projectMapper.toDTO(project);
    }

    private boolean isOwnerOrAdmin(User user, Project project) {
        return user.getEmail().equals(project.getOwner().getEmail()) || user.getRole().equals(Role.ADMIN);
    }
}
