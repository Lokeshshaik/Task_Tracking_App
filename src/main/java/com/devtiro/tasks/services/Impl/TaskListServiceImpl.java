package com.devtiro.tasks.services.Impl;

import com.devtiro.tasks.domain.entities.Task;
import com.devtiro.tasks.domain.entities.TaskList;
import com.devtiro.tasks.repositories.TaskListRepository;
import com.devtiro.tasks.services.TaskListService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListServiceImpl implements TaskListService {

    private TaskListRepository taskListRepository;
    TaskListServiceImpl(TaskListRepository taskListRepository){
        this.taskListRepository=taskListRepository;
    }

    @Override
    public List<TaskList> listAllTaskLists() {
        return taskListRepository.findAll();
    }

    @Override
    public TaskList createTaskList(TaskList taskList){
        if(null != taskList.getId()){
            throw new IllegalArgumentException("Task list already has an id!");
        }
        if(null == taskList.getTitle() || taskList.getTitle().isBlank()){
            throw new IllegalArgumentException("Task list must have a name!");
        }
        LocalDateTime now = LocalDateTime.now();
        return taskListRepository.save(
                new TaskList(
                     null,
                        taskList.getTitle(),
                        taskList.getDescription(),
                        now,
                        now,
                        null
                )
        );
    }

    @Override
    public Optional<TaskList> getTaskList(UUID id) {
        return taskListRepository.findById(id);
    }

    @Override
    public TaskList updateTaskList(UUID taskListId, TaskList taskList) {
        if(null == taskList.getId()){
            throw new IllegalArgumentException("Task List must have an ID");
        }
        //https://www.notion.so/290b30e2930c808daf9df60261c1f4e6
        if(!Objects.equals(taskListId,taskList.getId())){
            throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted");
        }

        TaskList existingTaskList = taskListRepository.findById(taskListId)
                .orElseThrow(()-> new IllegalArgumentException("Task list not found with this ID!"));
        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setUpdated(LocalDateTime.now());
        return taskListRepository.save(existingTaskList);
    }

    @Override
    public void deleteTaskList(UUID taskListId) {
        if(null == taskListId){
            throw new IllegalArgumentException("Task list not found");
        }
        taskListRepository.deleteById(taskListId);
    }


}
