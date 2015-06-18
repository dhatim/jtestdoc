package org.dhatim.jtestdoc.utilities;

import org.apache.tools.ant.BuildException;

public class ErrorManager {

    boolean blocking;

    public ErrorManager(boolean blocking) {
        this.blocking = blocking;
    }

    public void add(BuildException e) {
        if (blocking) {
            throw e;
        } else {
            System.out.println("WARNING : " + e.getMessage());
        }
    }
}
