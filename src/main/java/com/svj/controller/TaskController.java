package com.svj.controller;

import com.svj.dto.TaskDto;
import com.svj.entity.Task;
import com.svj.entity.TaskStatus;
import com.svj.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository repository;

    @Autowired
    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Long> createTask(@RequestBody TaskDto dto){
        Task task= convertToEntity(dto);
//        task.setTaskStatus(null); // set only title, description during creation
        Task savedInstance = repository.save(task);
        return new ResponseEntity<Long>(savedInstance.getId(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable Long id){
        Optional<Task> taskInDB = repository.findById(id);
        if(taskInDB.isPresent()){
            return new ResponseEntity<TaskDto>(taskInDB.get().toDto(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOne(@PathVariable Long id, @RequestBody TaskDto dto){
        Optional<Task> taskInDB = repository.findById(id);
        if(taskInDB.isPresent()){
            Task task = taskInDB.get();
            TaskDto dummy= task.toDto();
            if(dummy.getTitle()== null)
                task.setTitle(dto.getTitle());
            if(dummy.getDescription()== null)
                task.setDescription(dto.getDescription());
            try {
                if(dto.getStatus()== null)
                    throw new IllegalArgumentException();
                task.setTaskStatus(TaskStatus.valueOf(dto.getStatus()));
            }catch (IllegalArgumentException e){
                return new ResponseEntity<>("Available statuses are: CREATED, APPROVED, REJECTED, BLOCKED, DONE.", HttpStatus.BAD_REQUEST);
            }
            repository.save(task);
            return new ResponseEntity<>("", HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable Long id){
        Optional<Task> taskInDB = repository.findById(id);
        if(taskInDB.isPresent()){
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks(){
        Iterable<Task> tasks = repository.findAll();
        List<TaskDto> dtos= new LinkedList<>();
        for(Task obj: tasks){
            dtos.add(obj.toDto());
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    private Task convertToEntity(TaskDto dto) {
        Task task = new Task(dto.getTitle());
        // task.setId(Long.valueOf(dto.getId()));
        // task.setTitle();
        task.setDescription(dto.getDescription());
        // task.setTaskStatus(TaskStatus.valueOf(dto.getStatus()));
        return task;
    }

//    public static void main(String[] args) {
//        try {
//            TaskStatus val = TaskStatus.valueOf("arj");
//        }catch (IllegalArgumentException e){
//            System.out.println("Exc");
//        }
//    }
}
