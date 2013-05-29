/*
 * Logging.java
 *
 * Created on 6 juillet 2005, 11:24
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.exproxy.tools;

import java.io.IOException;
import java.util.logging.LogManager;

import java.io.FileInputStream;

/**
 *
 * @author David Crosson
 */
public class Logging {
    
    private static final String RESPATH="resource/logging.properties";

    public static void logSystemInit() throws IOException {
        LogManager lm = LogManager.getLogManager();

        lm.readConfiguration(new FileInputStream(RESPATH));
    }
}
