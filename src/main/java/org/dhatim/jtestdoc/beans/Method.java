package org.dhatim.jtestdoc.beans;

import java.util.List;

/**
 * The Method class is the model for the processed methods
 * 
 * @author Nathanaël Langlois
 * @version 1.0
 */
public class Method {

	private String name;
	private String initialState;
	private List<XStep> steps;
	private String description;

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

	public List<XStep> getSteps() {
		return steps;
	}

	public void setSteps(List<XStep> steps) {
		this.steps = steps;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
