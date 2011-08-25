/*
 * ErrorLog.java  
 *  
 * eFisa Project version 1.0 
 *  
 * Mar 30, 2011
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
import java.util.Calendar;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Clase utilizada para persistir registros de error el la base del jBPM 
 *
 * @author vogelb
 * @version 1.0
 */
@Entity
@Table(name="ErrorLog")
public class ErrorLog implements Serializable {

    private static final long serialVersionUID = 4259726003210499676L;

    @EmbeddedId
    private ErrorLogPK errorLogPK;
    
    private String errorMesage;
    
    private Calendar errorTimestamp;

    public ErrorLog() {
        errorLogPK = new ErrorLogPK();
    }

    /**
     * @return variable processInstanceId
     */
    public Long getProcessInstanceId() {
        return errorLogPK.getProcessInstanceId();
    }

    /**
     * @return variable sequence
     */
    public Integer getSequence() {
        return errorLogPK.getSequence();
    }

    /**
     * @return variable errorMesage
     */
    public String getErrorMesage() {
        return errorMesage;
    }

    /**
     * @return variable errorTimestamp
     */
    public Calendar getErrorTimestamp() {
        return errorTimestamp;
    }

    /**
     * @param processInstanceId variable processInstanceId a setear
     */
    public void setProcessInstanceId(Long processInstanceId) {
        this.errorLogPK.setProcessInstanceId(processInstanceId);
    }

    /**
     * @param sequence variable sequence a setear
     */
    public void setSequence(Integer sequence) {
        this.errorLogPK.setSequence(sequence);
    }

    /**
     * @param errorMesage variable errorMesage a setear
     */
    public void setErrorMesage(String errorMesage) {
        this.errorMesage = errorMesage;
    }

    /**
     * @param errorTimestamp variable errorTimestamp a setear
     */
    public void setErrorTimestamp(Calendar errorTimestamp) {
        this.errorTimestamp = errorTimestamp;
    }
}
