package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.UpdateTaskDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Task toEntity(TaskRequestDTO dto) {
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado"));
        User assignee = userRepository.findById(dto.assigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Responsável não encontrado"));
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
        task.setAssignee(assignee);
        return task;
    }

    public TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                userMapper.toDTO(task.getAssignee()),
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

        if (dto.assigneeId() != null) {
            User assignee = userRepository.findById(dto.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Responsável não encontrado"));
            task.setAssignee(assignee);
        }
    }
}
