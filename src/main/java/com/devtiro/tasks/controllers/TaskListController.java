package com.devtiro.tasks.controllers;

import com.devtiro.tasks.domain.dto.TasklistDto;
import com.devtiro.tasks.domain.entities.TaskList;
import com.devtiro.tasks.mappers.TaskListMapper;
import com.devtiro.tasks.services.TaskListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/task-lists")
public class TaskListController {
    private TaskListService taskListService;
    private TaskListMapper taskListMapper;

    public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper) {
        this.taskListService = taskListService;
        this.taskListMapper = taskListMapper;
    }

    @GetMapping
    public List<TasklistDto> listAllTaskLists(){
        return taskListService.listAllTaskLists().stream().map(taskListMapper::toDto).toList();
    }

    //https://www.notion.so/28fb30e2930c80fabb8cc19b6a2a5a33
    @PostMapping
    public TasklistDto createTaskList(@RequestBody TasklistDto tasklistDto){
        TaskList createdTaskList = taskListService.createTaskList(
                taskListMapper.fromDto(tasklistDto)
        );
        return taskListMapper.toDto(createdTaskList);
    }

    @GetMapping(path = "/{task_list_id}")
    public Optional<TasklistDto> getTaskList(@PathVariable("task_list_id") UUID taskListId){
        return taskListService.getTaskList(taskListId).map(taskListMapper::toDto);
    }

    @PutMapping(path = "/{task_list_id}")
    public TasklistDto updateTaskList(
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody TasklistDto tasklistDto
    ){
        TaskList updatedTaskList = taskListService.updateTaskList(
          taskListId,
          taskListMapper.fromDto(tasklistDto)
        );
        return taskListMapper.toDto(updatedTaskList);
    }

    @DeleteMapping(path = "/{task_list_id}")
    public void deleteTaskList(@PathVariable("task_list_id") UUID taskListId){
        taskListService.deleteTaskList(taskListId);
    }
}
