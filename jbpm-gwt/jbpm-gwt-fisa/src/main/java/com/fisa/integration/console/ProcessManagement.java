/*
 * ProcessManagement.java  
 *  
 * eFisa Project version 1.0 
 *  
 * Jun 6, 2011
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

package com.fisa.integration.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.drools.definition.process.Process;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;
import org.jboss.bpm.console.client.model.ProcessInstanceRef;
import org.jboss.bpm.console.client.model.ProcessInstanceRef.RESULT;
import org.jboss.bpm.console.client.model.ProcessInstanceRef.STATE;
import org.jbpm.integration.console.Transform;
import org.jbpm.process.audit.ProcessInstanceLog;


/**
 * extension to org.jbpm.integration.console.ProcessManagement 
 *
 * @author vogelb
 * @version 1.0
 */
public class ProcessManagement implements org.jboss.bpm.console.server.integration.ProcessManagement {

    private CommandDelegate delegate;
    
    public ProcessManagement() {
        delegate = new CommandDelegate();
    }

    public List<ProcessDefinitionRef> getProcessDefinitions() {
        List<Process> processes = delegate.getProcesses();
        List<ProcessDefinitionRef> result = new ArrayList<ProcessDefinitionRef>();
        for (Process process: processes) {
            result.add(Transform.processDefinition(process));
        }
        return result;
    }

    public ProcessDefinitionRef getProcessDefinition(String definitionId) {
        Process process = delegate.getProcess(definitionId);
        return Transform.processDefinition(process);
    }

    /**
     * method unsupported
     */
    public List<ProcessDefinitionRef> removeProcessDefinition(String definitionId) {
        delegate.removeProcess(definitionId); 
        return getProcessDefinitions();
    }

    public ProcessInstanceRef getProcessInstance(String instanceId) {
        ProcessInstanceLog processInstance = delegate.getProcessInstanceLog(instanceId);
        return Transform.processInstance(processInstance);
    }

    public List<ProcessInstanceRef> getProcessInstances(String definitionId) {
        List<ProcessInstanceLog> processInstances = delegate.getActiveProcessInstanceLogsByProcessId(definitionId);
        List<ProcessInstanceRef> result = new ArrayList<ProcessInstanceRef>();
        for (ProcessInstanceLog processInstance: processInstances) {
            result.add(Transform.processInstance(processInstance));
        }
        return result;
    }

    public ProcessInstanceRef newInstance(String definitionId) {
        ProcessInstanceLog processInstance = delegate.startProcess(definitionId, null);
        return Transform.processInstance(processInstance);
    }
    
    public ProcessInstanceRef newInstance(String definitionId, Map<String, Object> processVars) {
        ProcessInstanceLog processInstance = delegate.startProcess(definitionId, processVars);
        return Transform.processInstance(processInstance);
    }

    public void setProcessState(String instanceId, STATE nextState) {
        if (nextState == STATE.ENDED) {
            delegate.abortProcessInstance(instanceId);
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    public Map<String, Object> getInstanceData(String instanceId) {
        return delegate.getProcessInstanceVariables(instanceId);
    }

    public void setInstanceData(String instanceId, Map<String, Object> data) {
        delegate.setProcessInstanceVariables(instanceId, data);
    }

    
    public void signalExecution(String executionId, String signal) {
        delegate.signalExecution(executionId, signal);
    }

    public void deleteInstance(String instanceId) {
        delegate.abortProcessInstance(instanceId);
    }

    //result means nothing
    public void endInstance(String instanceId, RESULT result) {
        delegate.abortProcessInstance(instanceId);
    }

}
