package dev.matheuslf.desafio.inscritos.repository.specs;

import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class TaskSpec {

    public static Specification<Task> title(String title) {
        return (root, query, builder) -> builder.equal(root.get("title"), title);
    }

    public static Specification<Task> status(Status status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    public static Specification<Task> priority(Priority priority) {
        return (root, query, builder) -> builder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> projectId(UUID projectId) {
        return (root, query, builder) -> builder.equal(root.get("project").get("id"), projectId);
    }
}
