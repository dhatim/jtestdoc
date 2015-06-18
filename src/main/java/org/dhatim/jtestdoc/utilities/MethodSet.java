package org.dhatim.jtestdoc.utilities;

import java.util.ArrayList;
import java.util.List;

import org.dhatim.jtestdoc.beans.Method;
import org.dhatim.jtestdoc.beans.XStep;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;

public class MethodSet
{
	Method pmethod = new Method(); 
	List<XStep> steps = new ArrayList<XStep>();
	List<Method> methods = new ArrayList<Method>();
	List<MethodDeclaration> allmymethods = new ArrayList<MethodDeclaration>();
	List<Comment> assertComments = new ArrayList<Comment>();
	List<Comment> allComments = new ArrayList<Comment>();
	ErrorManager errorManager;
	
	public MethodSet(boolean blocking) {
		errorManager = new ErrorManager(blocking);
	}
	public Method getPmethod() {
		return pmethod;
	}
	public void setPmethod(Method pmethod) {
		this.pmethod = pmethod;
	}
	public List<XStep> getSteps() {
		return steps;
	}
	public void setSteps(List<XStep> steps) {
		this.steps = steps;
	}
	public List<Method> getMethods() {
		return methods;
	}
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
	public List<MethodDeclaration> getAllmymethods() {
		return allmymethods;
	}
	public void setAllmymethods(List<MethodDeclaration> allmymethods) {
		this.allmymethods = allmymethods;
	}
	public List<Comment> getAssertComments() {
		return assertComments;
	}
	public void setAssertComments(ArrayList<Comment> assertComments) {
		this.assertComments = assertComments;
	}
	public List<Comment> getAllComments() {
		return allComments;
	}
	public void setAllComments(List<Comment> allComments) {
		this.allComments = allComments;
	}
	public ErrorManager getErrorManager() {
		return errorManager;
	}
	public void setErrorManager(ErrorManager errorManager) {
		this.errorManager = errorManager;
	}
	
}
