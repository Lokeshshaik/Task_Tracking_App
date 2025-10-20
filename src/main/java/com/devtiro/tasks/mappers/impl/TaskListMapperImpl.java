package com.devtiro.tasks.mappers.impl;

import com.devtiro.tasks.domain.dto.TasklistDto;
import com.devtiro.tasks.domain.entities.Task;
import com.devtiro.tasks.domain.entities.TaskList;
import com.devtiro.tasks.domain.entities.TaskStatus;
import com.devtiro.tasks.mappers.TaskListMapper;
import com.devtiro.tasks.mappers.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskListMapperImpl implements TaskListMapper {
    private final TaskMapper taskMapper;

    public TaskListMapperImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskList fromDto(TasklistDto tasklistDto) {
        return new TaskList(
                tasklistDto.id(),
                tasklistDto.title(),
                tasklistDto.description(),
                null,
                null,
                Optional.ofNullable(tasklistDto.tasks())
                        .map(tasks -> tasks.stream()
                                .map(taskMapper::fromDto)
                                .toList())
                        .orElse(null)
        );
    }

    @Override
    public TasklistDto toDto(TaskList taskList) {
        return new TasklistDto(taskList.getId(),
        taskList.getTitle(),
        taskList.getDescription(),
        Optional.ofNullable(taskList.getTasks())
                .map(List::size)
                .orElse(0),
        calculateTaskListProgress(taskList.getTasks()),
        Optional.ofNullable(taskList.getTasks())
                .map(tasks -> tasks.stream().map(taskMapper::toDto).toList())
                .orElse(null));
    }

    private double calculateTaskListProgress(List<Task> tasks) {
        if(null == tasks){
            return 0;
        }
        long closedTaskCount = tasks.stream().filter(task -> task.getStatus() == TaskStatus.CLOSED).count();

        return (double)closedTaskCount/tasks.size();
    }
}
