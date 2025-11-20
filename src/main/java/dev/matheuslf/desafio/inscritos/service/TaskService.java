package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.UpdateTaskDTO;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.mapper.TaskMapper;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO save(TaskRequestDTO dto) {
        Task entity = taskMapper.toEntity(dto);
        Task savedTask = taskRepository.save(entity);
        return taskMapper.toDTO(savedTask);
    }

    public TaskResponseDTO update(UUID id, UpdateTaskDTO dto) {
        Task entity = taskRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Task not found"));
        taskMapper.updateEntity(entity, dto);
        Task savedTask = taskRepository.save(entity);
        return taskMapper.toDTO(savedTask);
    }
}
