package org.dhatim.jtestdoc.beans;

import java.util.ArrayList;

<<<<<<< HEAD
public class File {

    private String name;
    private ArrayList<Method> methods;

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

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }

=======
import org.dhatim.jtestdoc.beans.Method;


public class File 
{
	private String name;
	private ArrayList<Method> methods;
	
	public File(){}
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
	public ArrayList<Method> getMethods() {
		return methods;
	}
	public void setMethods(ArrayList<Method> methods) {
		this.methods = methods;
	}
	
	
>>>>>>> origin/master
}
