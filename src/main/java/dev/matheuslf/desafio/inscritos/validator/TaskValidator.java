package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.InvalidDateException;
import dev.matheuslf.desafio.inscritos.exception.ProjectEndedException;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskValidator {

    private final TaskRepository taskRepository;

    public void validateProjectEndDate(Project project) {
        if (project.getEndDate().isBefore(LocalDate.now())) {
            throw new ProjectEndedException("O projeto está finalizado");
        }
    }

    public void validateTaskDueDate(LocalDate taskDueDate, Project project) {
        if (taskDueDate != null && taskDueDate.isAfter(project.getEndDate())) {
            throw new InvalidDateException("A data de entrega da tarefa não pode ser posterior à data de término do projeto");
        }

        if (taskDueDate != null && taskDueDate.isBefore(project.getStartDate())) {
            throw new InvalidDateException("A data de entrega da tarefa não pode ser anterior à data de início do projeto");
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
