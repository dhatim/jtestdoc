package org.dhatim.jtestdoc.utilities;

import java.util.ArrayList;
import java.util.List;

import org.dhatim.jtestdoc.beans.Method;
import org.dhatim.jtestdoc.beans.XStep;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;

public class MethodSet {

    private List<XStep> steps = new ArrayList<>();
    private List<Method> testMethods = new ArrayList<>();
    private List<MethodDeclaration> allMethods = new ArrayList<>();
    private List<Comment> assertComments = new ArrayList<>();
    private List<Comment> allComments = new ArrayList<>();
    private ErrorManager errorManager;

    public MethodSet(boolean blocking) {
        errorManager = new ErrorManager(blocking);
    }

    public List<XStep> getSteps() {
        return steps;
    }

    public void setSteps(List<XStep> steps) {
        this.steps = steps;
    }

    public List<Method> getTestMethods() {
        return testMethods;
    }

    public void setTestMethods(List<Method> testMethods) {
        this.testMethods = testMethods;
    }

    public List<MethodDeclaration> getAllMethods() {
        return allMethods;
    }

    public void setAllMethods(List<MethodDeclaration> allmymethods) {
        this.allMethods = allmymethods;
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
