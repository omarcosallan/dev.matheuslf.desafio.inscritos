package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectUpdateDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.enums.Role;
import dev.matheuslf.desafio.inscritos.exception.BusinessException;
import dev.matheuslf.desafio.inscritos.exception.InvalidDateException;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
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

    private static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found with id: ";
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectValidator projectValidator;
    private final UserRepository userRepository;

    public ProjectResponseDTO save(ProjectRequestDTO dto) {
        Project project = projectMapper.toEntity(dto);
        projectValidator.validateProjectName(project);

        if (dto.endDate().isBefore(project.getStartDate())) {
            throw new InvalidDateException("Project's start date must be before the end date");
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

    public List<ProjectResponseDTO> findByOwnerOrAssignee(UUID userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + userId));
        return projectRepository.findByOwnerOrAssignee(owner).stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO update(User user, UUID id, ProjectUpdateDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE + id));

        if (!isOwnerOrAdmin(user, project)) {
            throw new BusinessException("You are not allowed to update this project");
        }

        projectValidator.validateProjectName(project);

        projectMapper.updateEntity(project, dto);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDTO(updatedProject);
    }

    public ProjectResponseDTO findById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE + id));
        return projectMapper.toDTO(project);
    }

    private boolean isOwnerOrAdmin(User user, Project project) {
        return user.getEmail().equals(project.getOwner().getEmail()) || user.getRole().equals(Role.ADMIN);
    }
}
