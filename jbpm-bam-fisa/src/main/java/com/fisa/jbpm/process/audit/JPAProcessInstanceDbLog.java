/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fisa.jbpm.process.audit;

import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.drools.persistence.TransactionManager;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAProcessInstanceDbLog extends org.jbpm.process.audit.JPAProcessInstanceDbLog {
	
    private static Logger logger = LoggerFactory.getLogger(JPAProcessInstanceDbLog.class);
    
    public JPAProcessInstanceDbLog(){
        super();
    }
    
    public JPAProcessInstanceDbLog(Environment env){
        super(env);
    }
    
    @SuppressWarnings("unchecked")
    public List<ProcessInstanceLog> findActiveProcessInstancesByParameters(String[] params) {
        UserTransaction ut;
        List<ProcessInstanceLog> result = null;
        try {
            ut = (UserTransaction) new InitialContext().lookup( "java:comp/UserTransaction" );
            ut.begin();
            result = getEntityManager().createQuery(
            "select distinct log.* from ProcessInstanceLog as log left outer join variableinstancelog as var on log.processinstanceid=var.processinstanceid where log.processId like ? and var.variableid='procvars' and var.value like ? AND log.end_date is null")
                .setParameter(1, "%"+params[0]+"%").setParameter(1, "%"+params[1]+"%").getResultList();
            ut.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Long> findChildInstances(Environment env, long processInstanceId) {
        TransactionManager txm = (TransactionManager)env.get(EnvironmentName.TRANSACTION_MANAGER);
        List<Long> result = null;
        try {
            txm.begin();
            result = getEntityManager().createNativeQuery(
                "select cast(substr(evt.element, 26) as long) from ProcessInstanceInfo as log join eventTypes as evt on log.instanceId=evt.instanceId where log.processinstanceid = ? and length(evt.element) > 20")
                    .setParameter(1, processInstanceId).getResultList();
            txm.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            try {
                if(txm != null && txm.getStatus()== Status.STATUS_ACTIVE) {
                    txm.rollback();
                }
            } catch (Exception ex) { /* ignore */ }
        }
        return result;
    }

    public Long findParentInstance(Environment env, long processInstanceId) {
//        TransactionManager txm = (TransactionManager)env.get(EnvironmentName.TRANSACTION_MANAGER);
        Long result = null;
        try {
//            txm.begin();
            result = (Long) getEntityManager().createNativeQuery(
                "select log.instanceId from ProcessInstanceInfo as log join eventTypes as evt on log.instanceId=evt.instanceId where evt.element = ?")
                    .setParameter(1, "processInstanceCompleted:"+processInstanceId).getSingleResult();
//            txm.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
//            try {
//                if(txm != null && txm.getStatus()== Status.STATUS_ACTIVE) {
//                    txm.rollback();
//                }
//            } catch (Exception ex) { /* ignore */ }
        }
        return result;
    }

    public CurrentProcessState findCurrentProcessState(long id) {
        try {
            return (CurrentProcessState) getEntityManager().createQuery("from CurrentProcessState as cps where cps.processInstanceId = ?").setParameter(1, id).getSingleResult();
        } catch (EntityNotFoundException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    public CurrentProcessState updateCurrentProcessState(CurrentProcessState parentState) {
        if(parentState.getId()==0L) {
            getEntityManager().persist(parentState);
//            getEntityManager().refresh(parentState);
        } else {
            parentState = getEntityManager().merge(parentState);
        }
        for(CurrentChildProcessState childState : parentState.getChildProcesses()) {
            childState = updateCurrentChildProcessState(childState);
            parentState.addChildProcess(childState);
        }
        return parentState;
    }

    public void deleteCurrentProcessState(long id) {
        try {
            @SuppressWarnings("unchecked")
            List<CurrentProcessState> parentStateList = (List<CurrentProcessState>) getEntityManager().createQuery("from CurrentProcessState as cps where cps.processInstanceId = ?").setParameter(1, id).getResultList();
            if(parentStateList != null && !parentStateList.isEmpty()) {
                for (CurrentProcessState parentState : parentStateList) {
                    for (CurrentChildProcessState childState : parentState.getChildProcesses()) {
                        getEntityManager().remove(childState);
                    }
                    getEntityManager().remove(parentState);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public CurrentChildProcessState findCurrentChildProcessStateByChildId(long childProcessId) {
        try {
            return (CurrentChildProcessState) getEntityManager().createQuery("from CurrentChildProcessState as cps where cps.processInstanceId = ?").setParameter(1, childProcessId).getSingleResult();
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<CurrentChildProcessState> findCurrentChildProcessStateByChildProcess(long childProcessId, String childProcessName) {
        return getEntityManager().createQuery("from CurrentChildProcessState as cps where cps.processTree like ?").
            setParameter(1, new StringBuilder("%").append(childProcessId).append(":").append(childProcessName).append("%").toString()).getResultList();
    }


    public CurrentChildProcessState updateCurrentChildProcessState(CurrentChildProcessState childState) {
        if(childState.getId()==0L) {
            getEntityManager().persist(childState);
//                getEntityManager().refresh(parentState);
        } else {
            childState = getEntityManager().merge(childState);
        }
        return childState;
    }

}
