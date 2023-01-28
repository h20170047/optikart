package com.svj.entity;

import com.svj.dto.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static com.svj.entity.TaskStatus.CREATED;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private TaskStatus status= CREATED;

    public Task(String title) {
        this.title= title;
    }

    public TaskDto toDto(){
        return new TaskDto(String.valueOf(id), title, description, status.name());
    }

    public void setTaskStatus(TaskStatus status){
        this.status= status;
    }
}
