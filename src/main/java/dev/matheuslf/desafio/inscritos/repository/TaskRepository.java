package dev.matheuslf.desafio.inscritos.repository;

import dev.matheuslf.desafio.inscritos.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
