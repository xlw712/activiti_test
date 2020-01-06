package com.levin.activiti.controller;

import com.levin.activiti.pojo.TaskRepresentation;
import com.levin.activiti.service.ActivitiService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * https://segmentfault.com/a/1190000020298085
 */
@RestController
@RequestMapping(value = "activiti")
public class ActivitiController {
    @Autowired
    private ActivitiService service;

    @RequestMapping(value = "start", method = RequestMethod.GET)
    public String start() {
        return service.start();
    }

    @RequestMapping(value = "task", method = RequestMethod.GET)
    public List<TaskRepresentation> getTask(@RequestParam(value = "uid") String uid) {
        List<Task> tasks = service.getTask(uid);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }
}
