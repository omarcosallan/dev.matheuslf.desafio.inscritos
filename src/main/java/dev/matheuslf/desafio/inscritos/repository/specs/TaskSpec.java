package dev.matheuslf.desafio.inscritos.repository.specs;

import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class TaskSpec {

    public static Specification<Task> create(String title, Status status, Priority priority, UUID projectId) {
        return Specification.allOf(
                title(title),
                status(status),
                priority(priority),
                projectId(projectId)
        );
    }

    private static Specification<Task> title(String title) {
        if (title == null) return null;
        return (root, query, builder) -> builder.equal(root.get("title"), title);
    }

    private static Specification<Task> status(Status status) {
        if (status == null) return null;
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    private static Specification<Task> priority(Priority priority) {
        if (priority == null) return null;
        return (root, query, builder) -> builder.equal(root.get("priority"), priority);
    }

    private static Specification<Task> projectId(UUID projectId) {
        if (projectId == null) return null;
        return (root, query, builder) -> builder.equal(root.get("project").get("id"), projectId);
    }
}
