package com.devtiro.tasks.mappers;

import com.devtiro.tasks.domain.dto.TaskDto;
import com.devtiro.tasks.domain.dto.TasklistDto;
import com.devtiro.tasks.domain.entities.TaskList;

public interface TaskListMapper {

    TaskList fromDto(TasklistDto tasklistDto);

    TasklistDto toDto(TaskList taskList);
}
