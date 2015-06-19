package org.dhatim.jtestdoc.utilities;

import org.apache.tools.ant.BuildException;

/**
 * The ErrorManager allows to choose to throw errors, blocking the task from going on, or simply print out warnings.
 * @author Nathanaël Langlois
 * @version 1.0
 */
public class ErrorManager {

    private boolean blocking;

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
