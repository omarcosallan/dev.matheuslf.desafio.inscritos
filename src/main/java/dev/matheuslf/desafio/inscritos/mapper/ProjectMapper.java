package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponseDTO toDTO(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate()
        );
    }

    public Project toEntity(ProjectRequestDTO dto) {
        return new Project(
                dto.name(),
                dto.description(),
                dto.startDate(),
                dto.endDate()
        );
    }

    public void updateEntity(Project project, UpdateProjectDTO dto) {
        if (dto.name() != null) {
            project.setName(dto.name());
        }

        if (dto.description() != null) {
            project.setDescription(dto.description());
        }

        if (dto.startDate() != null) {
            project.setStartDate(dto.startDate());
        }

        if (dto.endDate() != null) {
            project.setEndDate(dto.endDate());
        }
    }
}
