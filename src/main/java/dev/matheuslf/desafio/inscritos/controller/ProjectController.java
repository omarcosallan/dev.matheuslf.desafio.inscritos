package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> save(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO project = projectService.save(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(project.id())
                .toUri();
        return ResponseEntity.created(uri).body(project);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProjectResponseDTO>> findAll(@RequestParam(required = false, defaultValue = "0")
                                                            Integer page,
                                                                    @RequestParam(required = false, defaultValue = "10")
                                                            Integer size) {
        return ResponseEntity.ok(projectService.findAll(page, size));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ProjectResponseDTO>> findByOwner(@PathVariable(value = "ownerId") UUID ownerId) {
        return ResponseEntity.ok(projectService.findByOwner(ownerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid UpdateProjectDTO dto) {
        return ResponseEntity.ok(projectService.update(id, dto));
    }
}
