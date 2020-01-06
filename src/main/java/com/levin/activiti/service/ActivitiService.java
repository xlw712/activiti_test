package com.levin.activiti.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivitiService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    /**
     * start activiti process
     *
     * @return instance id
     */
    public String start() {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("SimpleProcess");
        return instance.getId();
    }

    /**
     * get user task list
     *
     * @param uid
     * @return user task list
     */
    public List<Task> getTask(String uid) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(uid).list();
        return tasks;
    }
}
