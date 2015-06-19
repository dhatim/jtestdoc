package org.dhatim.jtestdoc.utilities;

import org.apache.tools.ant.BuildException;

public class ErrorManager {

    boolean blocking;

    public ErrorManager(boolean blocking) {
        this.blocking = blocking;
    }
    /**
     * This method throws an exception or a warning with a given BuildException depending on the object's parameter
     * @param e the exception thrown
     */
    public void add(BuildException e) {
        if (blocking) {
            throw e;
        } else {
            System.out.println("WARNING : " + e.getMessage());
        }
    }
}
