package org.dhatim.jtestdoc.beans;

import java.util.ArrayList;
import java.util.List;

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
