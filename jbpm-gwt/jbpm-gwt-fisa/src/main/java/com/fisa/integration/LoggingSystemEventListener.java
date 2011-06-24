/*
 * LoggerSystemEventListener.java  
 *  
 * eFisa Project version 1.0 
 *  
 * Mar 25, 2011
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

package com.fisa.integration;

import org.drools.SystemEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase o interfaz utilizada para .................... ejemplo de Utilizacion si es necesario 
 *
 * @author vogelb
 * @version 1.0
 */
public class LoggingSystemEventListener implements SystemEventListener {
    
    private static Logger logger = LoggerFactory.getLogger(LoggingSystemEventListener.class);

    /* (non-Javadoc)
     * @see org.drools.SystemEventListener#info(java.lang.String)
     */
    public void info(String message) {
        logger.info(message);
    }

    /* (non-Javadoc)
     * @see org.drools.SystemEventListener#info(java.lang.String, java.lang.Object)
     */
    public void info(String message, Object object) {
        if(object instanceof Throwable) {
            logger.info(message, (Throwable) object);
        } else {
            logger.info(message + " " + object.toString());
        }
    }

    /* (non-Javadoc)
     * @see org.drools.SystemEventListener#warning(java.lang.String)
     */
    public void warning(String message) {
        logger.warn(message);
    }

    /* (non-Javadoc)
     * @see org.drools.SystemEventListener#warning(java.lang.String, java.lang.Object)
     */
    public void warning(String message, Object object) {
        if(object instanceof Throwable) {
            logger.warn(message, (Throwable) object);
        } else {
            logger.warn(message + " " + object.toString());
        }
    }

    /* (non-Javadoc)
     * @see org.drools.SystemEventListener#exception(java.lang.String, java.lang.Throwable)
     */
    public void exception(String message, Throwable e) {
        logger.error(message, e);
    }

    /* (non-Javadoc)
     * @see org.drools.SystemEventListener#exception(java.lang.Throwable)
     */
    public void exception(Throwable e) {
        logger.error(e.getMessage(), e);
    }

    /* (non-Javadoc)
     * @see org.drools.SystemEventListener#debug(java.lang.String)
     */
    public void debug(String message) {
        logger.debug(message);
    }

    /* (non-Javadoc)
     * @see org.drools.SystemEventListener#debug(java.lang.String, java.lang.Object)
     */
    public void debug(String message, Object object) {
        if(object instanceof Throwable) {
            logger.debug(message, (Throwable) object);
        } else {
            logger.debug(message + " " + object.toString());
        }
    }

}
