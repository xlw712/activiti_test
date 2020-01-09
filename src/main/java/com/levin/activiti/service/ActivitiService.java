package com.levin.activiti.service;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivitiService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;

    /**
     * start activiti process
     *
     * @return instance id
     */
    public String start() {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("SimpleProcess");
        return instance.getId();
    }

    public String startByKey(String key, Map variables) {
        return runtimeService.startProcessInstanceByKey(key, variables).getId();
    }

    public List<ProcessDefinition> processlist() {
        List<ProcessDefinition> processDefinition = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc().latestVersion().list();
        for (ProcessDefinition pd : processDefinition) {
            System.out.println("----------------------------------------------");
            System.out.println("流程定义名：" + pd.getName());
            System.out.println("流程定义版本：" + pd.getVersion());
            System.out.println("流程定义KEY：" + pd.getKey());
            System.out.println("流程部署Deploymentid：" + pd.getDeploymentId());
            System.out.println("流程定义id：" + pd.getId());
        }
        return processDefinition;
    }

    /**
     * getTodoTasks
     *
     * @param uid
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<Task> getToDoTasks(String uid, int firstResult, int maxResults) {
        return taskService.createTaskQuery().taskAssignee(uid).listPage(firstResult, maxResults);
    }

    /**
     * getDoneTasks
     *
     * @param uid
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<HistoricTaskInstance> getDoneTasks(String uid, int firstResult, int maxResults) {
        return historyService.createHistoricTaskInstanceQuery().taskAssignee(uid).finished().listPage(firstResult, maxResults);
    }

    /**
     * getToDoTotal
     *
     * @param uid
     * @return
     */
    public long getToDoTotal(String uid) {
        return taskService.createTaskQuery().taskAssignee(uid).count();
    }

    /**
     * getDoneTotal
     *
     * @param uid
     * @return
     */
    public long getDoneTotal(String uid) {
        return historyService.createHistoricTaskInstanceQuery().taskAssignee(uid).count();
    }

    /**
     * completeTaskById
     *
     * @param taskId
     */
    public void completeTaskById(String taskId) {
        taskService.complete(taskId);
    }

    public String deploy(String name, InputStream fin) {
        String deploymentId = repositoryService.createDeployment()
                .addInputStream(name, fin)
                .name(name)
                .key(name)
                .deploy()
                .getId();
        return deploymentId;
    }

    public List<Task> processVariableValueLike(String uid, String likeStr) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(uid).includeProcessVariables().processVariableValueLike("title", likeStr).list();
        for (Task t : tasks) {
            System.out.println(t);
        }
        return tasks;
//        return taskService.createTaskQuery().taskAssignee(uid).processVariableValueLike("title", likeStr).list();
    }
}
