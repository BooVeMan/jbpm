package com.fisa.jbpm.process.audit;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ErrorLogPK implements Serializable {

    private static final long serialVersionUID = -4598536119810064738L;

    private Long processInstanceId;
    
    private Integer sequence;
    
    public ErrorLogPK() {
    }

    /**
     * @return variable processInstanceId
     */
    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    /**
     * @return variable sequence
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * @param processInstanceId variable processInstanceId a setear
     */
    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    /**
     * @param sequence variable sequence a setear
     */
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((processInstanceId == null) ? 0 : processInstanceId.hashCode());
        result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ErrorLogPK other = (ErrorLogPK) obj;
        if (processInstanceId == null) {
            if (other.processInstanceId != null)
                return false;
        } else if (!processInstanceId.equals(other.processInstanceId))
            return false;
        if (sequence == null) {
            if (other.sequence != null)
                return false;
        } else if (!sequence.equals(other.sequence))
            return false;
        return true;
    }

}