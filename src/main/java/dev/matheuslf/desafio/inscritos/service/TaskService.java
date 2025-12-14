package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskStatusUpdateDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Role;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.exception.BusinessException;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.mapper.TaskMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.matheuslf.desafio.inscritos.repository.specs.TaskSpec.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private static final String TASK_NOT_FOUND_MESSAGE = "Task not found with id: ";
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskResponseDTO save(TaskRequestDTO dto) {
        Task task = taskMapper.toEntity(dto);

        if (existsRegisteredTask(task)) {
            throw new ConflictException("There is already a task with this name in this project");
        }

        if (task.getProject().getEndDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Project has already ended");
        }

        task.getProject().getAssignees().add(task.getAssignee());

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public TaskResponseDTO updateStatus(User user, UUID id, TaskStatusUpdateDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(TASK_NOT_FOUND_MESSAGE + id));

        if (!(user.getRole().equals(Role.ADMIN) || task.getAssignee().getEmail().equals(user.getEmail()))) {
            throw new BusinessException("You do not have permission to update the status of this task");
        }

        if (task.getStatus().equals(Status.DONE)) {
            throw new ConflictException("It is not possible to modify an completed task");
        }

        task.setStatus(Status.valueOf(dto.status()));
        Task savedTask = taskRepository.save(task);
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
        Task task = taskRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(TASK_NOT_FOUND_MESSAGE + id));

        if (task.getProject().getEndDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Project has already ended");
        }

        if (task.getDueDate().isAfter(LocalDate.now())) {
            throw new ConflictException("It is not possible to modify an expired task");
        }

        if (task.getStatus().equals(Status.DONE)) {
            throw new ConflictException("It is not possible to modify an completed task");
        }

        taskRepository.delete(task);
    }

    public List<TaskResponseDTO> findByProject(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow( () -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return taskRepository.findAllByProject(project).stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    public TaskResponseDTO findById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(TASK_NOT_FOUND_MESSAGE + id));

        return taskMapper.toDTO(task);
    }

    public List<TaskResponseDTO> findByAssignee(UUID assigneeId) {
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow( () -> new ResourceNotFoundException("Assignee not found with id: " + assigneeId));

        return taskRepository.findAllByAssignee(assignee).stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    private boolean existsRegisteredTask(Task task) {
        Optional<Task> foundTask = taskRepository.findByTitleAndProject(task.getTitle(), task.getProject());

        if (task.getId() == null) {
            return foundTask.isPresent();
        }

        return foundTask.isPresent() && !task.getId().equals(foundTask.get().getId());
    }
}
