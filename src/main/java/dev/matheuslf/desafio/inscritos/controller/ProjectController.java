package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.pagination.PageResponse;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectSimpleResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectUpdateDTO;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
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
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> save(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO project = projectService.save(dto);
        URI uri = GenerateURI.generate(project.id());
        return ResponseEntity.created(uri).body(project);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProjectSimpleResponseDTO>> findAll(@RequestParam(required = false, defaultValue = "0")
                                                                    Integer page,
                                                                    @RequestParam(required = false, defaultValue = "10")
                                                                    Integer size) {
        return ResponseEntity.ok(projectService.findAll(page, size));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectSimpleResponseDTO>> findByOwnerOrAssignee(@PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.ok(projectService.findByOwnerOrAssignee(userId));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectResponseDTO> findById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(@AuthenticationPrincipal
                                                     UserDetails userDetails,
                                                     @PathVariable(value = "id")
                                                     UUID id,
                                                     @Valid @RequestBody
                                                     ProjectUpdateDTO dto) {
        return ResponseEntity.ok(projectService.update((User) userDetails, id, dto));
    }
}
