package org.dhatim.jtestdoc.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * The File class is the model for the processed files
 * @author Nathanaël Langlois
 * @version 1.0
 */
public class File {

    private String name;
    private List<Method> methods;

    public File() {
    }

    public File(String name, ArrayList<Method> methods) {
        super();
        this.name = name;
        this.methods = methods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

}
