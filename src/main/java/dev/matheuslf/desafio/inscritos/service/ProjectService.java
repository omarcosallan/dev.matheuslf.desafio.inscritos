package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponseDTO save(ProjectRequestDTO dto) {
        Project entity = projectMapper.toEntity(dto);
        Project savedProject = projectRepository.save(entity);
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
}
