package com.levin.activiti.controller;

import com.levin.activiti.pojo.TaskRepresentation;
import com.levin.activiti.service.ActivitiService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "startByKey", method = RequestMethod.GET)
    public String startByKey(@RequestParam(value = "key") String key) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("inputUser", "xiaoliwen");
        return service.startByKey(key, variables);
    }

    @RequestMapping(value = "processList", method = RequestMethod.GET)
    public List<ProcessDefinition> processList() {
        return service.processlist();
    }
    @RequestMapping(value = "completeTaskById", method = RequestMethod.GET)
    public void completeTaskById(@RequestParam(value = "taskId") String taskId) {
        service.completeTaskById(taskId);
    }

    @RequestMapping(value = "todoTask", method = RequestMethod.GET)
    public List<TaskRepresentation> getToDoTasks(@RequestParam(value = "uid") String uid, @RequestParam(value = "firstResult") int firstResult, @RequestParam(value = "maxResults")
            int maxResults) {
        List<Task> tasks = service.getToDoTasks(uid, firstResult, maxResults);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }

    @RequestMapping(value = "doneTask", method = RequestMethod.GET)
    public List<TaskRepresentation> getDoneTasks(@RequestParam(value = "uid") String uid, @RequestParam(value = "firstResult") int firstResult, @RequestParam(value = "maxResults")
            int maxResults) {
        List<HistoricTaskInstance> tasks = service.getDoneTasks(uid, firstResult, maxResults);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (HistoricTaskInstance task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }

    @RequestMapping(value = "getToDoTotal")
    public long getToDoTotal(@RequestParam(value = "uid") String uid) {
        return service.getToDoTotal(uid);
    }

    @RequestMapping(value = "getDoneTotal")
    public long getDoneTotal(@RequestParam(value = "uid") String uid) {
        return service.getDoneTotal(uid);
    }

    /**
     * curl -X POST -F 'file=@WechatIMG1.jpeg' http://localhost:8080/deploy
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/deploy")
    public String deploy(@RequestParam("file") MultipartFile file) {
        try {
            return service.deploy(file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("upload failed");
        }
    }

    @PostMapping(value = "/deployBpmn")
    public String deployBpmn(@RequestParam("file") MultipartFile file) {
        try {
            String name = file.getOriginalFilename();
            if (!name.endsWith(".bpmn20.xm") && !name.endsWith(".bpmn")) {
                name = name + ".bpmn";
            }
            return service.deploy(name, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("upload failed");
        }
    }
}
