package org.dhatim.jtestdoc.visitors;

import org.apache.tools.ant.BuildException;
import org.dhatim.jtestdoc.utilities.MethodSet;

import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * The TestMethodVisitor is in charge of finding a method's expected results
 * @author Nathanaël Langlois
 * @version 1.0
 */
public class TestMethodVisitor extends VoidVisitorAdapter<MethodSet> {

    private MethodSet methodSet;
	/**
	 * This method checks if a line has a call to another method
	 * @param node
	 */
    private void recursiveSearch(ExpressionStmt node) {
        methodSet.getAllMethods()
                .stream()
                .filter((mm) -> (((MethodCallExpr)node.getExpression()).getName().equals(mm.getName())))
                .forEach((mm) -> {
                    visit(mm, null);
                });
    }

    /**
     * This method tries to find the test's documentation and acts recursively if there are methods calls inside the method
     */
    @Override
    public void visit(ExpressionStmt expressionStmt, MethodSet methodSet) {
        this.methodSet = methodSet;
        if (expressionStmt.getExpression() instanceof MethodCallExpr) {
            if (((MethodCallExpr) (expressionStmt.getExpression())).getName().contains("assert")) {
                if (expressionStmt.getComment() != null) {
                    methodSet.getAssertComments().add(expressionStmt.getComment());
                } else {
                    boolean found = false;
                    for (Comment c : methodSet.getAllComments()) {
                        if (c.getEndLine() + 1 == expressionStmt.getBeginLine()) {
                            c.setContent(c.getContent().replace("\"", "'"));
                            methodSet.getAssertComments().add(c);
                            found = true;
                        } else if (c.getContent().startsWith("Expected result:")) {
                            c.setContent(c.getContent().substring(16).replace("\"", "'"));
                            if (!methodSet.getAssertComments().contains(c)) {
                                methodSet.getAssertComments().add(c);
                            }
                        }
                    }
                    if (!found) {
                        methodSet.getErrorManager().add(new BuildException("You have an undocumented asserts line " + expressionStmt.getBeginLine()));
                    }
                }
            } else {
                recursiveSearch(expressionStmt);
            }
        }

    }
}
