/*
 * CurrentProcessState.java  
 *  
 * eFisa Project version 1.0 
 *  
 * Aug 18, 2011
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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Clase o interfaz utilizada para .................... ejemplo de Utilizacion si es necesario 
 *
 * @author vogelb
 * @version 1.0
 */
@Entity
public class CurrentChildProcessState implements Serializable {

    private static final long serialVersionUID = -6663860443573537574L;

    private long id;
    private CurrentProcessState parentProcess;
    private long processInstanceId;
    private String processName;
    private long nodeInstanceId;
    private String nodeName;
    private String processTree;
    private String signal;
    private Date date;
    /**
     * 
     */
    public CurrentChildProcessState() {
        this.date = new Date();
    }

    /**
     * @param parentProcess
     * @param childProcessInstanceId
     * @param childProcessName
     * @param nodeInstanceId
     * @param nodeName
     * @param processTree
     * @param signal
     */
    public CurrentChildProcessState(CurrentProcessState parentProcess, long childProcessInstanceId,
            String childProcessName, long nodeInstanceId, String nodeName, String processTree, String signal) {
        this.parentProcess = parentProcess;
        this.processInstanceId = childProcessInstanceId;
        this.processName = childProcessName;
        this.nodeInstanceId = nodeInstanceId;
        this.nodeName = nodeName;
        this.processTree = processTree;
        this.signal = signal;
        this.date = new Date();
    }
    
    /**
     * @param processInstanceId
     * @param processName
     * @param nodeInstanceId
     * @param nodeName
     * @param processTree
     * @param signal
     */
    public CurrentChildProcessState(long processInstanceId,
            String processName, long nodeInstanceId, String nodeName, String processTree, String signal) {
        this.processInstanceId = processInstanceId;
        this.processName = processName;
        this.nodeInstanceId = nodeInstanceId;
        this.nodeName = nodeName;
        this.processTree = processTree;
        this.signal = signal;
        this.date = new Date();
    }

    /**
     * @return variable id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * @return variable parentProcess
     */
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="parentId", nullable=false)
    public CurrentProcessState getParentProcess() {
        return parentProcess;
    }

    /**
     * @return variable processInstanceId
     */
    public long getProcessInstanceId() {
        return processInstanceId;
    }

    /**
     * @return variable processName
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * @return variable nodeInstanceId
     */
    public long getNodeInstanceId() {
        return nodeInstanceId;
    }

    /**
     * @return variable nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @return variable processTree
     */
    public String getProcessTree() {
        return processTree;
    }

    /**
     * @return variable signal
     */
    public String getSignal() {
        return signal;
    }

    /**
     * @return variable date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "log_date")
    public Date getDate() {
        return date;
    }

    /**
     * @param id variable id a setear
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @param parentProcess variable parentProcess a setear
     */
    public void setParentProcess(CurrentProcessState parentProcess) {
        this.parentProcess = parentProcess;
    }

    /**
     * @param processInstanceId variable processInstanceId a setear
     */
    public void setProcessInstanceId(long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    /**
     * @param processName variable processName a setear
     */
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    /**
     * @param nodeInstanceId variable nodeInstanceId a setear
     */
    public void setNodeInstanceId(long nodeInstanceId) {
        this.nodeInstanceId = nodeInstanceId;
    }

    /**
     * @param nodeName variable nodeName a setear
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @param processTree variable processTree a setear
     */
    public void setProcessTree(String processTree) {
        this.processTree = processTree;
    }

    /**
     * @param signal variable signal a setear
     */
    public void setSignal(String signal) {
        this.signal = signal;
    }

    /**
     * @param date variable date a setear
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    
}
