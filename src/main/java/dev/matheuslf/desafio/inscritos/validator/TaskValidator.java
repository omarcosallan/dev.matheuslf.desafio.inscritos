package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskValidator {

    private final TaskRepository taskRepository;

    public void validateTaskDueDate(Task task) {
        if (task.getDueDate().isAfter(LocalDate.now())) {
            throw new ConflictException("Não é possível modificar uma tarefa expirada");
        }
    }

    public void validateTaskStatus(Task task) {
        if (task.getStatus().equals(Status.DONE)) {
            throw new ConflictException("Não é possível modificar uma tarefa concluída");
        }
    }

    public void validateTaskName(Task task) {
        if (existsRegisteredTask(task)) {
            throw new ConflictException("Já existe uma tarefa com esse nome neste projeto");
        }
    }

    private boolean existsRegisteredTask(Task task) {
        Optional<Task> foundTask = taskRepository.findByTitleAndProject(task.getTitle(), task.getProject());

        if (task.getId() == null) {
            return foundTask.isPresent();
        }

        return foundTask.isPresent() && !task.getId().equals(foundTask.get().getId());
    }
}
