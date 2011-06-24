/*
 * ManagementFactory.java  
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

import org.jbpm.integration.console.TaskManagement;
import org.jbpm.integration.console.UserManagement;

/**
 * Clase o interfaz utilizada para .................... ejemplo de Utilizacion si es necesario 
 *
 * @author vogelb
 * @version 1.0
 */
public class ManagementFactory extends org.jboss.bpm.console.server.integration.ManagementFactory {

    /* (non-Javadoc)
     * @see org.jbpm.integration.console.ManagementFactory#createProcessManagement()
     */
    @Override
    public ProcessManagement createProcessManagement() {
        return new ProcessManagement();
    }

    public TaskManagement createTaskManagement() {
        return new TaskManagement();
    }

    public UserManagement createUserManagement() {
        return new UserManagement();
    }


}
