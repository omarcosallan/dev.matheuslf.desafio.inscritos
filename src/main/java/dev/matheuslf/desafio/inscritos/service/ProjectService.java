package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectSimpleResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectUpdateDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.enums.Role;
import dev.matheuslf.desafio.inscritos.exception.BusinessException;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.InvalidDateException;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found with id: ";
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;

    @Transactional
    public ProjectResponseDTO save(ProjectRequestDTO dto) {
        User owner = userService.findByEmail(dto.ownerEmail());

        Project project = projectMapper.toEntity(dto);
        project.setOwner(owner);

        if (existsRegisteredProject(project)) {
            throw new ConflictException("There is already a project with this name");
        }

        if (dto.endDate().isBefore(project.getStartDate())) {
            throw new InvalidDateException("Project's start date must be before the end date");
        }

        Project savedProject = save(project);

        return projectMapper.toDTO(savedProject);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public PageResponse<ProjectSimpleResponseDTO> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projects = projectRepository.findAll(pageable);

        return new PageResponse<>(
                projectMapper.toDTO(projects.getContent()),
                projects.getNumber(),
                projects.getTotalPages(),
                projects.getTotalElements(),
                size,
                projects.hasNext(),
                projects.hasPrevious()
        );
    }

    public List<ProjectSimpleResponseDTO> findByOwnerOrAssignee(UUID userId) {
        User owner = userService.findById(userId);
        return projectMapper.toDTO(owner.getProjects().stream().toList());
    }

    @Transactional
    public ProjectResponseDTO update(User user, UUID id, ProjectUpdateDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE + id));

        if (!isOwnerOrAdmin(user, project)) {
            throw new BusinessException("You are not allowed to update this project");
        }

        if (existsRegisteredProject(project)) {
            throw new ConflictException("There is already a project with this name");
        }

        if (dto.startDate() != null && dto.endDate() != null) {
            if (dto.endDate().isBefore(dto.startDate())) {
                throw new InvalidDateException("Project's start date must be before the end date");
            }
        } else if (dto.startDate() != null) {
            if (project.getEndDate().isBefore(dto.startDate())) {
                throw new InvalidDateException("Project's new start date cannot be after the current end date");
            }
        } else if (dto.endDate() != null) {
            if (dto.endDate().isBefore(project.getStartDate())) {
                throw new InvalidDateException("Project's new end date cannot be before the current start date");
            }
        }

        if (dto.ownerEmail() != null) {
            User newOwner = userService.findByEmail(dto.ownerEmail());
            project.setOwner(newOwner);
        }

        projectMapper.updateEntity(dto, project);
        Project updatedProject = save(project);

        return projectMapper.toDTO(updatedProject);
    }

    public ProjectResponseDTO findById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE + id));

        return projectMapper.toDTO(project);
    }

    public Project findByName(String name) {
        return projectRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with name: " + name));
    }

    private boolean isOwnerOrAdmin(User user, Project project) {
        return user.getEmail().equals(project.getOwner().getEmail()) || user.getRole().equals(Role.ADMIN);
    }

    private boolean existsRegisteredProject(Project project) {
        Optional<Project> foundProject = projectRepository.findByName(project.getName());

        if (project.getId() == null) {
            return foundProject.isPresent();
        }

        return foundProject.isPresent() && !project.getId().equals(foundProject.get().getId());
    }
}
