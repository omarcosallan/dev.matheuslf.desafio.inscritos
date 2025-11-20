package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;

public class ProjectMapper {

    public static ProjectResponseDTO toDTO(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate()
        );
    }

    public static Project toEntity(ProjectRequestDTO dto) {
        return new Project(
                dto.name(),
                dto.description(),
                dto.startDate(),
                dto.endDate()
        );
    }
}
