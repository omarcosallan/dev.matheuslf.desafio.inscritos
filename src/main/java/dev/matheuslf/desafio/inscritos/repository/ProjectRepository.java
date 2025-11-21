package dev.matheuslf.desafio.inscritos.repository;

import dev.matheuslf.desafio.inscritos.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByName(String name);
}
