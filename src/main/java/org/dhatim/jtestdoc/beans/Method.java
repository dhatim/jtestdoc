package org.dhatim.jtestdoc.beans;

import java.util.ArrayList;

import org.dhatim.jtestdoc.beans.XStep;

public class Method 
{
	public String name;
	public String initialState;
	public ArrayList<XStep> steps;
	public String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInitialState() {
		return initialState;
	}
	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}
	public ArrayList<XStep> getSteps() {
		return steps;
	}
	public void setSteps(ArrayList<XStep> steps) {
		this.steps = steps;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
