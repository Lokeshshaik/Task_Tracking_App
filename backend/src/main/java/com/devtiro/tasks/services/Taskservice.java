package com.devtiro.tasks.services;

import com.devtiro.tasks.domain.entities.Task;

import java.util.List;
import java.util.UUID;

public interface Taskservice {
    List<Task> listTasks(UUID taskListId);
    Task createTask(UUID taskListId, Task task);
    Task getTask(UUID taskListId, UUID taskId);
    Task updateTask(UUID taskListId, UUID taskId, Task task);
    void deletetask(UUID taskListId, UUID taskId);
}
