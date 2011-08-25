/*
 * JPAWorkingMemoryDbLogger.java  
 *  
 * eFisa Project version 1.0 
 *  
 * Aug 11, 2011
 * 
 * Copyright (c) 2006 FISA Group.
 *
 * Todos los derechos reservados.
 * This software is the confidential and proprietary information FISA GROUP
 * ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FISA Group.
*/

package com.fisa.jbpm.process.audit;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.drools.WorkingMemory;
import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.runtime.process.NodeInstance;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.jbpm.workflow.instance.node.EventNodeInstance;
import org.jbpm.workflow.instance.node.SubProcessNodeInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fisa.jbpm.process.audit.Constants.STATE;

/**
 * Clase o interfaz utilizada para .................... ejemplo de Utilizacion si es necesario 
 *
 * @author vogelb
 * @version 1.0
 */
public class JPAWorkingMemoryDbLogger extends org.jbpm.process.audit.JPAWorkingMemoryDbLogger {
    
    private static Logger logger = LoggerFactory.getLogger(JPAWorkingMemoryDbLogger.class);
    
    private JPAProcessInstanceDbLog processInstanceLogger;

    public JPAWorkingMemoryDbLogger(WorkingMemory workingMemory) {
        super(workingMemory);
        processInstanceLogger = new JPAProcessInstanceDbLog(env);
    }

    public JPAWorkingMemoryDbLogger(KnowledgeRuntimeEventManager session) {
        super(session);
        processInstanceLogger = new JPAProcessInstanceDbLog(env);
    }

    /* (non-Javadoc)
     * @see org.drools.audit.WorkingMemoryLogger#beforeProcessStarted(org.drools.event.process.ProcessStartedEvent)
     */
    @Override
    public void beforeProcessStarted(ProcessStartedEvent event) {
        logger.debug(">>>************** beforeProcessStarted *****************");
        logger.debug("ProcessInstanceId:"+event.getProcessInstance().getId());
        logger.debug("ProcessId:"+event.getProcessInstance().getProcessId());
        logger.debug("ProcessName:"+event.getProcessInstance().getProcessName());
        logger.debug("ProcessState:"+STATE.values()[event.getProcessInstance().getState()].toString());
        logger.debug("ProcessEvents:"+Arrays.asList(event.getProcessInstance().getEventTypes()));
        logger.debug("<<<************** beforeProcessStarted *****************");
        super.beforeProcessStarted(event);
    }

    /* (non-Javadoc)
     * @see org.drools.audit.WorkingMemoryLogger#afterProcessStarted(org.drools.event.process.ProcessStartedEvent)
     */
    @Override
    public void afterProcessStarted(ProcessStartedEvent event) {
        logger.debug(">>>************** afterProcessStarted ******************");
        logger.debug("ProcessInstanceId:"+event.getProcessInstance().getId());
        logger.debug("ProcessId:"+event.getProcessInstance().getProcessId());
        logger.debug("ProcessName:"+event.getProcessInstance().getProcessName());
        logger.debug("ProcessState:"+STATE.values()[event.getProcessInstance().getState()].toString());
        logger.debug("ProcessEvents:"+Arrays.asList(event.getProcessInstance().getEventTypes()));
        logger.debug("<<<************** afterProcessStarted ******************");
        super.afterProcessStarted(event);
    }

    /* (non-Javadoc)
     * @see org.drools.audit.WorkingMemoryLogger#beforeProcessCompleted(org.drools.event.process.ProcessCompletedEvent)
     */
    @Override
    public void beforeProcessCompleted(ProcessCompletedEvent event) {
        logger.debug(">>>************* beforeProcessCompleted ****************");
        logger.debug("ProcessInstanceId:"+event.getProcessInstance().getId());
        logger.debug("ProcessId:"+event.getProcessInstance().getProcessId());
        logger.debug("ProcessName:"+event.getProcessInstance().getProcessName());
        logger.debug("ProcessState:"+STATE.values()[event.getProcessInstance().getState()].toString());
        logger.debug("ProcessEvents:"+Arrays.asList(event.getProcessInstance().getEventTypes()));
        logger.debug("<<<************* beforeProcessCompleted ****************");
        super.beforeProcessCompleted(event);
    }

    /* (non-Javadoc)
     * @see org.drools.audit.WorkingMemoryLogger#afterProcessCompleted(org.drools.event.process.ProcessCompletedEvent)
     */
    @Override
    public void afterProcessCompleted(ProcessCompletedEvent event) {
        logger.debug(">>>************* afterProcessCompleted *****************");
        logger.debug("ProcessInstanceId:"+event.getProcessInstance().getId());
        logger.debug("ProcessId:"+event.getProcessInstance().getProcessId());
        logger.debug("ProcessName:"+event.getProcessInstance().getProcessName());
        logger.debug("ProcessState:"+STATE.values()[event.getProcessInstance().getState()].toString());
        logger.debug("ProcessEvents:"+Arrays.asList(event.getProcessInstance().getEventTypes()));
        logger.debug("<<<************* afterProcessCompleted *****************");
        super.afterProcessCompleted(event);
    }

    /* (non-Javadoc)
     * @see org.drools.audit.WorkingMemoryLogger#beforeNodeTriggered(org.drools.event.process.ProcessNodeTriggeredEvent)
     */
    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
        if( event.getNodeInstance() instanceof SubProcessNodeInstance ||
                event.getNodeInstance() instanceof EventNodeInstance ) {
            logger.debug(">>>************** beforeNodeTriggered ******************");
            logger.debug("ProcessInstanceId:"+event.getProcessInstance().getId());
            logger.debug("ProcessId:"+event.getProcessInstance().getProcessId());
            logger.debug("ProcessName:"+event.getProcessInstance().getProcessName());
            logger.debug("ProcessState:"+STATE.values()[event.getProcessInstance().getState()].toString());
            logger.debug("ProcessEvents:"+Arrays.asList(event.getProcessInstance().getEventTypes()));
            logger.debug("************************* Node *************************");
            if(event.getNodeInstance() instanceof SubProcessNodeInstance) {
                SubProcessNodeInstance subProcessNode = (SubProcessNodeInstance) event.getNodeInstance();
                logger.debug("NodeInstanceId:"+subProcessNode.getId());
                logger.debug("NodeId:"+subProcessNode.getNodeId());
                logger.debug("NodeName:"+subProcessNode.getNodeName());
                logger.debug("NodeClassName:"+subProcessNode.getClass().getName());
                logger.debug("NodeInstanceContainer:"+subProcessNode.getNodeInstanceContainer());
                if(subProcessNode.getProcessInstanceId()>0) {
                    ProcessInstance subProcess = subProcessNode.getProcessInstance().getKnowledgeRuntime().getProcessInstance(subProcessNode.getProcessInstanceId());
                    if(subProcess != null) {
                        logger.debug("********************** subProcess **********************");
                        logger.debug("ProcessInstanceId:"+subProcess.getId());
                        logger.debug("ProcessId:"+subProcess.getProcessId());
                        logger.debug("ProcessName:"+subProcess.getProcessName());
                        logger.debug("ProcessState:"+STATE.values()[subProcess.getState()].toString());
                        logger.debug("ProcessEvents:"+Arrays.asList(subProcess.getEventTypes()));
                    }
                }
            } else {
                EventNodeInstance eventNode = (EventNodeInstance) event.getNodeInstance();
                logger.debug("NodeInstanceId:"+eventNode.getId());
                logger.debug("NodeId:"+eventNode.getNodeId());
                logger.debug("NodeName:"+eventNode.getNodeName());
                logger.debug("NodeClassName:"+eventNode.getClass().getName());
                logger.debug("NodeInstanceContainer:"+eventNode.getNodeInstanceContainer());
            }
            logger.debug("<<<************** beforeNodeTriggered ******************");
        }
        super.beforeNodeTriggered(event);
    }

    /* (non-Javadoc)
     * @see org.drools.audit.WorkingMemoryLogger#afterNodeTriggered(org.drools.event.process.ProcessNodeTriggeredEvent)
     */
    @Override
    public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
        if( event.getNodeInstance() instanceof SubProcessNodeInstance ||
                event.getNodeInstance() instanceof EventNodeInstance ) {
            logger.debug(">>>*************** afterNodeTriggered ******************");
            logger.debug("ProcessInstanceId:"+event.getProcessInstance().getId());
            logger.debug("ProcessId:"+event.getProcessInstance().getProcessId());
            logger.debug("ProcessName:"+event.getProcessInstance().getProcessName());
            logger.debug("ProcessState:"+STATE.values()[event.getProcessInstance().getState()].toString());
            logger.debug("ProcessEvents:"+Arrays.asList(event.getProcessInstance().getEventTypes()));
            logger.debug("************************* Node *************************");
            if(event.getNodeInstance() instanceof SubProcessNodeInstance) {
                SubProcessNodeInstance subProcessNode = (SubProcessNodeInstance) event.getNodeInstance();
                logger.debug("NodeInstanceId:"+subProcessNode.getId());
                logger.debug("NodeId:"+subProcessNode.getNodeId());
                logger.debug("NodeName:"+subProcessNode.getNodeName());
                logger.debug("NodeClassName:"+subProcessNode.getClass().getName());
                logger.debug("NodeInstanceContainer:"+subProcessNode.getNodeInstanceContainer());
                if(subProcessNode.getProcessInstanceId()>0) {
                    ProcessInstance subProcess = subProcessNode.getProcessInstance().getKnowledgeRuntime().getProcessInstance(subProcessNode.getProcessInstanceId());
                    if(subProcess != null) {
                        logger.debug("********************** subProcess **********************");
                        logger.debug("ProcessInstanceId:"+subProcess.getId());
                        logger.debug("ProcessId:"+subProcess.getProcessId());
                        logger.debug("ProcessName:"+subProcess.getProcessName());
                        logger.debug("ProcessState:"+STATE.values()[subProcess.getState()].toString());
                        logger.debug("ProcessEvents:"+Arrays.asList(subProcess.getEventTypes()));
                    }
                }
            } else {
                EventNodeInstance eventNode = (EventNodeInstance) event.getNodeInstance();
                logger.debug("NodeInstanceId:"+eventNode.getId());
                logger.debug("NodeId:"+eventNode.getNodeId());
                logger.debug("NodeName:"+eventNode.getNodeName());
                logger.debug("NodeClassName:"+eventNode.getClass().getName());
                logger.debug("NodeInstanceContainer:"+eventNode.getNodeInstanceContainer());
            }
            logger.debug("<<<*************** afterNodeTriggered ******************");
        }
        super.afterNodeTriggered(event);
    }

    /* (non-Javadoc)
     * @see org.drools.audit.WorkingMemoryLogger#beforeNodeLeft(org.drools.event.process.ProcessNodeLeftEvent)
     */
    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent event) {
        if( event.getNodeInstance() instanceof SubProcessNodeInstance ||
                event.getNodeInstance() instanceof EventNodeInstance ) {
            logger.debug(">>>***************** beforeNodeLeft ********************");
            logger.debug("ProcessInstanceId:"+event.getProcessInstance().getId());
            logger.debug("ProcessId:"+event.getProcessInstance().getProcessId());
            logger.debug("ProcessName:"+event.getProcessInstance().getProcessName());
            logger.debug("ProcessState:"+STATE.values()[event.getProcessInstance().getState()].toString());
            logger.debug("ProcessEvents:"+Arrays.asList(event.getProcessInstance().getEventTypes()));
            logger.debug("************************* Node *************************");
            if(event.getNodeInstance() instanceof SubProcessNodeInstance) {
                SubProcessNodeInstance subProcessNode = (SubProcessNodeInstance) event.getNodeInstance();
                logger.debug("NodeInstanceId:"+subProcessNode.getId());
                logger.debug("NodeId:"+subProcessNode.getNodeId());
                logger.debug("NodeName:"+subProcessNode.getNodeName());
                logger.debug("NodeClassName:"+subProcessNode.getClass().getName());
                logger.debug("NodeInstanceContainer:"+subProcessNode.getNodeInstanceContainer());
                if(subProcessNode.getProcessInstanceId()>0) {
                    ProcessInstance subProcess = subProcessNode.getProcessInstance().getKnowledgeRuntime().getProcessInstance(subProcessNode.getProcessInstanceId());
                    if(subProcess != null) {
                        logger.debug("********************** subProcess **********************");
                        logger.debug("ProcessInstanceId:"+subProcess.getId());
                        logger.debug("ProcessId:"+subProcess.getProcessId());
                        logger.debug("ProcessName:"+subProcess.getProcessName());
                        logger.debug("ProcessState:"+STATE.values()[subProcess.getState()].toString());
                        logger.debug("ProcessEvents:"+Arrays.asList(subProcess.getEventTypes()));
                    }
                }
            } else {
                EventNodeInstance eventNode = (EventNodeInstance) event.getNodeInstance();
                logger.debug("NodeInstanceId:"+eventNode.getId());
                logger.debug("NodeId:"+eventNode.getNodeId());
                logger.debug("NodeName:"+eventNode.getNodeName());
                logger.debug("NodeClassName:"+eventNode.getClass().getName());
                logger.debug("NodeInstanceContainer:"+eventNode.getNodeInstanceContainer());
            }
            logger.debug("<<<***************** beforeNodeLeft ********************");
        }
        super.beforeNodeLeft(event);
    }

    /* (non-Javadoc)
     * @see org.drools.audit.WorkingMemoryLogger#afterNodeLeft(org.drools.event.process.ProcessNodeLeftEvent)
     */
    @Override
    public void afterNodeLeft(ProcessNodeLeftEvent event) {
        if( event.getNodeInstance() instanceof SubProcessNodeInstance ||
                event.getNodeInstance() instanceof EventNodeInstance ) {
            logger.debug(">>>***************** afterNodeLeft *********************");
            logger.debug("ProcessInstanceId:"+event.getProcessInstance().getId());
            logger.debug("ProcessId:"+event.getProcessInstance().getProcessId());
            logger.debug("ProcessName:"+event.getProcessInstance().getProcessName());
            logger.debug("ProcessState:"+STATE.values()[event.getProcessInstance().getState()].toString());
            logger.debug("ProcessEvents:"+Arrays.asList(event.getProcessInstance().getEventTypes()));
            logger.debug("************************* Node *************************");
            if(event.getNodeInstance() instanceof SubProcessNodeInstance) {
                SubProcessNodeInstance subProcessNode = (SubProcessNodeInstance) event.getNodeInstance();
                logger.debug("NodeInstanceId:"+subProcessNode.getId());
                logger.debug("NodeId:"+subProcessNode.getNodeId());
                logger.debug("NodeName:"+subProcessNode.getNodeName());
                logger.debug("NodeClassName:"+subProcessNode.getClass().getName());
                logger.debug("NodeInstanceContainer:"+subProcessNode.getNodeInstanceContainer());
                if(subProcessNode.getProcessInstanceId()>0) {
                    ProcessInstance subProcess = subProcessNode.getProcessInstance().getKnowledgeRuntime().getProcessInstance(subProcessNode.getProcessInstanceId());
                    if(subProcess != null) {
                        logger.debug("********************** subProcess **********************");
                        logger.debug("ProcessInstanceId:"+subProcess.getId());
                        logger.debug("ProcessId:"+subProcess.getProcessId());
                        logger.debug("ProcessName:"+subProcess.getProcessName());
                        logger.debug("ProcessState:"+STATE.values()[subProcess.getState()].toString());
                        logger.debug("ProcessEvents:"+Arrays.asList(subProcess.getEventTypes()));
                    }
                }
            } else {
                EventNodeInstance eventNode = (EventNodeInstance) event.getNodeInstance();
                logger.debug("NodeInstanceId:"+eventNode.getId());
                logger.debug("NodeId:"+eventNode.getNodeId());
                logger.debug("NodeName:"+eventNode.getNodeName());
                logger.debug("NodeClassName:"+eventNode.getClass().getName());
                logger.debug("NodeInstanceContainer:"+eventNode.getNodeInstanceContainer());
            }
            logger.debug("<<<****************** afterNodeLeft ********************");
        }
        super.afterNodeLeft(event);
    }

    public void logProcessTree(ProcessInstance parentProcessInstance, ProcessInstance processInstance) {
        if(parentProcessInstance.equals(processInstance)) {
            CurrentProcessState parentState = processInstanceLogger.findCurrentProcessState(parentProcessInstance.getId());
            if(parentState!=null) { /* for process continuation we need to recreate the child tree */
                parentState.setChildProcesses(null);
                parentState.setDate(new Date());
                processInstanceLogger.updateCurrentProcessState(parentState);
            }
        }
        if(processInstance.getEventTypes()!=null) {
            for (String event : processInstance.getEventTypes()) {
                if(event.startsWith("processInstanceCompleted:")) {
                    WorkflowProcessInstance jbpmProcessInstance = (WorkflowProcessInstance) processInstance;
                    WorkflowProcessInstance childProcessInstance = (WorkflowProcessInstance) jbpmProcessInstance.getKnowledgeRuntime().getProcessInstance(Long.parseLong(event.substring(25)));
                    if(STATE.ACTIVE.equals(STATE.values()[childProcessInstance.getState()])) {
                        logProcessTree(parentProcessInstance, childProcessInstance);
                    }
                }
            }
        }
        CurrentProcessState parentState = processInstanceLogger.findCurrentProcessState(parentProcessInstance.getId());
        if(parentState==null) {
            parentState = new CurrentProcessState(parentProcessInstance.getId(), parentProcessInstance.getProcess().getName(), null, parentProcessInstance.getState());
            if(parentProcessInstance instanceof WorkflowProcessInstance) {
                WorkflowProcessInstance jbpmProcessInstance = (WorkflowProcessInstance) parentProcessInstance;
                if(jbpmProcessInstance.getVariable("procvars")!=null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> procvars = (Map<String, Object>) jbpmProcessInstance.getVariable("procvars"); 
                    parentState.setRequestId((String) procvars.get("request_id"));
                }
            }
            processInstanceLogger.updateCurrentProcessState(parentState);
        }
        if(processInstance.getEventTypes()!=null) { /* all processes in a safe state SHOULD have events */
            for (String event : processInstance.getEventTypes()) {
                List<CurrentChildProcessState> childStateList = null;
                WorkflowProcessInstance jbpmProcessInstance = (WorkflowProcessInstance) processInstance;
                if(event.startsWith("processInstanceCompleted:")) {
                    WorkflowProcessInstance childProcessInstance = (WorkflowProcessInstance) jbpmProcessInstance.getKnowledgeRuntime().getProcessInstance(Long.parseLong(event.substring(25)));
                    childStateList = processInstanceLogger.findCurrentChildProcessStateByChildProcess(childProcessInstance.getId(), childProcessInstance.getProcessName());
                } else {
                    NodeInstance nodeInstance = null;
                    for(NodeInstance node : jbpmProcessInstance.getNodeInstances()) {
                        if(node instanceof EventNodeInstance && ((EventNodeInstance) node).getEventNode().getType().equals(event)) {
                            nodeInstance = node;
                            break;
                        }
                    }
                    CurrentChildProcessState childState = null;
                    if(nodeInstance!= null) {
                        childState = new CurrentChildProcessState(processInstance.getId(), processInstance.getProcessName(), nodeInstance.getId(), nodeInstance.getNodeName(), processInstance.getId()+":"+processInstance.getProcessName(), event);
                    } else {
                        childState = new CurrentChildProcessState(processInstance.getId(), processInstance.getProcessName(), 0L, null, processInstance.getId()+":"+processInstance.getProcessName(), null);
                    }
                    parentState.addChildProcess(childState);
                    childState = processInstanceLogger.updateCurrentChildProcessState(childState);
                }
                if(childStateList!=null && !childStateList.isEmpty()) {
                    for (CurrentChildProcessState childState : childStateList) {
                        childState.setProcessTree(new StringBuilder(Long.toString(processInstance.getId())).append(":").append(processInstance.getProcessName()).append("|").append(childState.getProcessTree()).toString());
                        parentState.addChildProcess(childState);
                    }
                }
            }
            processInstanceLogger.updateCurrentProcessState(parentState);
        }
    }

    public Long findParentProcess(Long processInstanceId) {
        Long parentProcessInstanceId = processInstanceId;
        while(true) {
            Long tempInstanceId = processInstanceLogger.findParentInstance(env, parentProcessInstanceId);
            if(tempInstanceId==null) {
                break;
            } else {
                parentProcessInstanceId = tempInstanceId;
            }
        }
        return parentProcessInstanceId;
    }

    public void deleteProcessTree(ProcessInstance parentProcessInstance) {
        processInstanceLogger.deleteCurrentProcessState(parentProcessInstance.getId());
    }
    
}
