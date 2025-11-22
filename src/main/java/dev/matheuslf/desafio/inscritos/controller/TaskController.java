package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.UpdateTaskDTO;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> save(@Valid @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO task = taskService.save(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task.id())
                .toUri();
        return ResponseEntity.created(uri).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable("id") UUID id, @RequestBody @Valid UpdateTaskDTO dto) {
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDTO>> findByProject(@PathVariable(value = "projectId") UUID projectId) {
        return ResponseEntity.ok(taskService.findByProject(projectId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<TaskResponseDTO>> findAllWithParams(
            @RequestParam(value = "title", required = false)
            String title,
            @RequestParam(value = "status", required = false)
            Status status,
            @RequestParam(value = "priority", required = false)
            Priority priority,
            @RequestParam(value = "projectId", required = false)
            UUID projectId,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "size", defaultValue = "10")
            Integer size
    ) {
        return ResponseEntity.ok(taskService.findAllWithParams(title, status, priority, projectId, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
