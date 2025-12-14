package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskStatusUpdateDTO;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import dev.matheuslf.desafio.inscritos.utils.GenerateURI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
        URI uri = GenerateURI.generate(task.id());
        return ResponseEntity.created(uri).body(task);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(@AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable("id") UUID id,
                                                        @Valid @RequestBody TaskStatusUpdateDTO dto) {
        return ResponseEntity.ok(taskService.updateStatus((User) userDetails, id, dto));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDTO>> findByProject(@PathVariable(value = "projectId") UUID projectId) {
        return ResponseEntity.ok(taskService.findByProject(projectId));
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<List<TaskResponseDTO>> findByAssignee(@PathVariable("assigneeId") UUID assigneeId) {
        return ResponseEntity.ok(taskService.findByAssignee(assigneeId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<TaskResponseDTO>> findAllWithParams(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) UUID projectId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(taskService.findAllWithParams(title, status, priority, projectId, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
