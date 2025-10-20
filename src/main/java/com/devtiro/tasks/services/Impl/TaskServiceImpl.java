package com.devtiro.tasks.services.Impl;

import com.devtiro.tasks.domain.entities.Task;
import com.devtiro.tasks.domain.entities.TaskList;
import com.devtiro.tasks.domain.entities.TaskPriority;
import com.devtiro.tasks.domain.entities.TaskStatus;
import com.devtiro.tasks.repositories.TaskListRepository;
import com.devtiro.tasks.repositories.TaskRepository;
import com.devtiro.tasks.services.Taskservice;

import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements Taskservice {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<Task> listTasks(UUID taskListId) {
        return taskRepository.findByTaskListId(taskListId);
    }

    @Override
    public Task createTask(UUID taskListId, Task task) {
        if(null != task.getId()){
            throw new IllegalArgumentException("Task already has an ID");
        }
        if(task.getTitle() == null || task.getTitle().isBlank()){
            throw new IllegalArgumentException("Task cannot be created unnamed");
        }
        TaskPriority taskPriority = Optional.ofNullable(task.getPriority())
                .orElse(TaskPriority.MEDIUM);
        TaskStatus taskStatus = TaskStatus.OPEN;

        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(()-> new IllegalArgumentException("Task list not found"));

        LocalDateTime now = LocalDateTime.now();

        Task taskToSave = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                taskStatus,
                taskPriority,
                now,
                now,
                taskList
        );

        return taskRepository.save(taskToSave);
    }

    @Override
    public Task getTask(UUID taskListId, UUID taskId) {

        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(()-> new IllegalArgumentException("Task list not found"));
        return taskList.getTasks()
                .stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Task not found inside the tasklist"));

    }

    @Override
    public Task updateTask(UUID taskListId, UUID taskId, Task task) {
        if(null == task.getId()){
            throw new IllegalArgumentException("Task must have an id");
        }
        if(!Objects.equals(taskId,task.getId())){
            throw new IllegalArgumentException("Attempting to change task id, not allowed");
        }
        if(null == task.getPriority()){
            throw new IllegalArgumentException("Task must have a valid priority");
        }
        if(null == task.getStatus()){
            throw new IllegalArgumentException("Task must have a valid status");
        }

        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(()-> new IllegalArgumentException("Task List not found"));
        Task taskToSave = taskList.getTasks().stream().filter(task1 -> task1.getId().equals(taskId)).findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Task not found"));

        taskToSave.setTitle(task.getTitle());
        taskToSave.setDescription(task.getDescription());
        taskToSave.setDueDate(task.getDueDate());
        taskToSave.setPriority(task.getPriority());
        taskToSave.setStatus(task.getStatus());
        taskToSave.setUpdated(LocalDateTime.now());

        return taskRepository.save(taskToSave);
    }

    @Override
    public void deletetask(UUID taskListId, UUID taskId) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(()-> new IllegalArgumentException("Task List not found"));
        Task taskToBeDeleted = taskList.getTasks().stream().filter(task -> task.getId().equals(taskId)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Task not found "));
        /*
        // The issue: Deleted task reappears when refresh because it's still in the TaskList's tasks collection in memory.
        // When you save taskList again, JPA may re-sync the collection and restore the deleted task.
        // Correct approach:
        // 1. Find the TaskList by ID
                TaskList taskList = taskListRepository.findById(taskListId)
                        .orElseThrow(() -> new IllegalArgumentException("Task List not found"));
        // 2. Find the task to delete
                Task taskToBeDeleted = taskList.getTasks().stream()
                        .filter(task -> task.getId().equals(taskId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        // 3. Remove the task from the tasks list in memory
                taskList.getTasks().remove(taskToBeDeleted);
        // 4. Delete the task from the repository
                taskRepository.delete(taskToBeDeleted);
        // 5. Save the updated task list
                taskListRepository.save(taskList);
        // Notes:
        // - Using taskList.getTasks().remove(...) ensures JPA does not restore the deleted task.
        // - Best practice: In TaskList entity, use orphanRemoval = true:
        //   @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
        //   private List<Task> tasks;
        // - With orphanRemoval = true, removing from the list automatically deletes the task from DB.
        */
        // When deleting a task, also remove it from the TaskList's Java list.
        // If you only delete it from the database, it might reappear when you refresh the browser.
        // This happens because the TaskList still holds the task in its in-memory list,
        // and saving the TaskList again can make JPA restore the deleted task.
        // Always remove it from the list, and consider using orphanRemoval in your mappings(when you're working with them).
        // If you want to know what happens just comment the below line, the deleted task appears after refresh
        taskList.getTasks().remove(taskToBeDeleted);
        taskRepository.delete(taskToBeDeleted);
        taskListRepository.save(taskList);
    }
}
