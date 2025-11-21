package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.UpdateTaskDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public Task toEntity(TaskRequestDTO dto) {
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado"));
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        if (dto.status() != null) {
            task.setStatus(Status.valueOf(dto.status().toUpperCase()));
        }
        if (dto.priority() != null) {
            task.setPriority(Priority.valueOf(dto.priority().toUpperCase()));
        }
        task.setDueDate(dto.dueDate());
        task.setProject(project);
        return task;
    }

    public TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                projectMapper.toDTO(task.getProject())
        );
    }

    public void updateEntity(Task task, UpdateTaskDTO dto) {
        if (dto.title() != null) {
            task.setTitle(dto.title());
        }

        if (dto.description() != null) {
            task.setDescription(dto.description());
        }

        if (dto.status() != null) {
            task.setStatus(Status.valueOf(dto.status().toUpperCase()));
        }

        if (dto.priority() != null) {
            task.setPriority(Priority.valueOf(dto.priority().toUpperCase()));
        }

        if (dto.dueDate() != null) {
            task.setDueDate(dto.dueDate());
        }
    }
}
