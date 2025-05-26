package com.apiRestFull.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.apiRestFull.todosimple.models.Task;
import com.apiRestFull.todosimple.models.User;
import com.apiRestFull.todosimple.repositories.TaskRepository;

import jakarta.transaction.Transactional;

public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);

        return task.orElseThrow(() -> new RuntimeException(
            "Tarefa não encontrada. ID: " + id + ", Tipo: " + Task.class.getName()
        ));
    }

    @Transactional
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());

        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = this.findById(obj.getId());

        newObj.setDescription(obj.getDescription());

        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        this.findById(id);

        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(
                "Não é possível deletar porque há usuários relacionados"
            );
        }
    }
}
