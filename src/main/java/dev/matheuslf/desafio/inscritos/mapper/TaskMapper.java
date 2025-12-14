package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProjectMapper.class})
public interface TaskMapper {

    TaskResponseDTO toDTO(Task task);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStringToStatus")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "mapStringToPriority")
    Task toEntity(TaskRequestDTO dto);

    @Named("mapStringToStatus")
    default Status mapStringToStatus(String status) {
        if (status == null) {
            return null;
        }
        return Status.valueOf(status.toUpperCase());
    }

    @Named("mapStringToPriority")
    default Priority mapStringToPriority(String priority) {
        if (priority == null) {
            return null;
        }
        return Priority.valueOf(priority.toUpperCase());
    }
}
