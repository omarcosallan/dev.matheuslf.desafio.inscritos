package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.UpdateTaskDTO;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.mapper.TaskMapper;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.matheuslf.desafio.inscritos.repository.specs.TaskSpec.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private static final String TASK_NOT_FOUND_MESSAGE = "Tarefa não encontrada";
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO save(TaskRequestDTO dto) {
        Task entity = taskMapper.toEntity(dto);
        Task savedTask = taskRepository.save(entity);
        return taskMapper.toDTO(savedTask);
    }

    public TaskResponseDTO update(UUID id, UpdateTaskDTO dto) {
        Task entity = getTaskById(id);
        taskMapper.updateEntity(entity, dto);
        Task savedTask = taskRepository.save(entity);
        return taskMapper.toDTO(savedTask);
    }

    public PageResponse<TaskResponseDTO> findAllWithParams(String title, Status status, Priority priority, UUID projectId, Integer page, Integer size) {
        Specification<Task> specs = null;

        if (title != null) {
            specs = title(title);
        }

        if (status != null) {
            specs = specs == null ? status(status) : specs.and(status(status));
        }

        if (priority != null) {
            specs = specs == null ? priority(priority) : specs.and(priority(priority));
        }

        if (projectId != null) {
            specs = (specs == null) ? projectId(projectId) : specs.and(projectId(projectId));
        }

        Pageable pageRequest = PageRequest.of(page, size);

        Page<Task> tasks = taskRepository.findAll(specs, pageRequest);

        return new PageResponse<>(
                tasks.getContent().stream().map(taskMapper::toDTO).toList(),
                tasks.getNumber(),
                tasks.getTotalPages(),
                tasks.getTotalElements(),
                tasks.getSize(),
                tasks.hasNext(),
                tasks.hasPrevious()
        );
    }

    public void delete(UUID id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    private Task getTaskById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(TASK_NOT_FOUND_MESSAGE));
    }
}
