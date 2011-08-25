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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Clase o interfaz utilizada para .................... ejemplo de Utilizacion si es necesario 
 *
 * @author vogelb
 * @version 1.0
 */
@Entity
public class CurrentProcessState implements Serializable {


    private static final long serialVersionUID = 3338407368587998469L;

    private long id;
    private long processInstanceId;
    private String processName;
    private Set<CurrentChildProcessState> childProcesses;
    //private String processTree; - do it in toString?
    private String requestId;
    private int state;
    private Date date;

    public CurrentProcessState() {
        this.childProcesses = new HashSet<CurrentChildProcessState>();
        this.date = new Date();
    }

    /**
     * @param id
     * @param processInstanceId
     * @param processName
     * @param requestId
     * @param state
     */
    public CurrentProcessState(long processInstanceId, String processName, String requestId, int state) {
        this.processInstanceId = processInstanceId;
        this.processName = processName;
        this.requestId = requestId;
        this.state = state;

        this.childProcesses = new HashSet<CurrentChildProcessState>();
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
     * @return variable childProcesses
     */
    @OneToMany(mappedBy="parentProcess", cascade=CascadeType.ALL)
    public Set<CurrentChildProcessState> getChildProcesses() {
        return childProcesses;
    }

    /**
     * @return variable requestId
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * @return variable state
     */
    public int getState() {
        return state;
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
     * @param childProcesses variable childProcesses a setear
     */
    public void setChildProcesses(Set<CurrentChildProcessState> childProcesses) {
        this.childProcesses = childProcesses;
    }

    /**
     * @param requestId variable requestId a setear
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * @param state variable state a setear
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * @param date variable date a setear
     */
    public void setDate(Date date) {
        this.date = date;
    }

    public void addChildProcess(CurrentChildProcessState childProcess) {
        if(childProcesses == null) {
            this.childProcesses = new HashSet<CurrentChildProcessState>();
        }
        childProcess.setParentProcess(this);
        childProcesses.add(childProcess);
    }

    public boolean removeChildProcess(CurrentChildProcessState childProcess) {
        if(childProcesses == null) {
            return false;
        }
        return childProcesses.remove(childProcess);
    }

}
