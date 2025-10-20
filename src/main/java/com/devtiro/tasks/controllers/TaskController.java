package com.devtiro.tasks.controllers;

import com.devtiro.tasks.domain.dto.TaskDto;
import com.devtiro.tasks.domain.entities.Task;
import com.devtiro.tasks.mappers.TaskMapper;
import com.devtiro.tasks.services.Taskservice;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/task-lists/{task_list_id}/tasks")
public class TaskController {

    private Taskservice taskservice;
    private TaskMapper taskMapper;

    public TaskController(Taskservice taskservice, TaskMapper taskMapper) {
        this.taskservice = taskservice;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public List<TaskDto> listTasks(@PathVariable("task_list_id")UUID taskListId){
        return taskservice.listTasks(taskListId).stream().map(taskMapper::toDto).toList();
    }

    @PostMapping
    public TaskDto createTask(@PathVariable("task_list_id")UUID taskListId, @RequestBody TaskDto taskDto){
        Task createdTask = taskservice.createTask(taskListId,taskMapper.fromDto(taskDto));
        return taskMapper.toDto(createdTask);
    }

    @GetMapping(path = "/{task_id}")
    public TaskDto getTask(@PathVariable("task_list_id")UUID taskListId, @PathVariable("task_id")UUID taskId){

        return taskMapper.toDto(taskservice.getTask(taskListId,taskId));
    }

    @PutMapping(path = "/{task_id}")
    public TaskDto updateTask(@PathVariable("task_list_id")UUID taskListId, @PathVariable("task_id")UUID taskId,
                              @RequestBody TaskDto taskDto){
        Task updatedTask =  taskservice.updateTask(taskListId,taskId,taskMapper.fromDto(taskDto));
        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping(path = "/{task_id}")
    public void deleteTask(@PathVariable("task_list_id")UUID taskListId, @PathVariable("task_id")UUID taskId){
        taskservice.deletetask(taskListId,taskId);
    }
}
