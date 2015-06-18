package org.dhatim.jtestdoc.visitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.tools.ant.BuildException;
import org.dhatim.jtestdoc.beans.Method;
import org.dhatim.jtestdoc.beans.XStep;
import org.dhatim.jtestdoc.utilities.MethodSet;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class TestTagVisitor extends VoidVisitorAdapter<MethodSet> {

    private MethodSet methodSet = new MethodSet(true);

    @Override
    public void visit(NormalAnnotationExpr marker, MethodSet m) {
        this.methodSet = m;
        if ("Test".equals(marker.getName().getName())) //Checking for the annotation @Test on the methods
        {
            process(
                    (MethodDeclaration) marker.getParentNode(),
                    marker.getPairs().stream()
                    .filter(pair -> "description".equals(pair.getName()))
                    .map(MemberValuePair::getValue)
                    .findFirst()
                    .map(StringLiteralExpr.class::cast)
                    .map(StringLiteralExpr::getValue)); //Processing the one having it
        }
    }

    @Override
    public void visit(MarkerAnnotationExpr marker, MethodSet m) {
        this.methodSet = m;
        if ("Test".equals(marker.getName().getName())) //Checking for the annotation @Test on the methods
        {
            process((MethodDeclaration) marker.getParentNode(), Optional.empty()); //Processing the one having it
        }
    }

    protected void process(MethodDeclaration methodDeclaration, Optional<String> markerDescription) throws BuildException {
        if (methodDeclaration == null) {
            return;
        }

        Method processedMethod = new Method();

        if (markerDescription.isPresent()) {
            processedMethod.setDescription(markerDescription.get().replace("\"", "'"));
        } else if (methodDeclaration.getComment() != null) {
            processedMethod.setDescription(methodDeclaration.getComment().getContent().replace("\"", "'").replace("*", ""));
        } else {
            methodSet.getErrorManager().add(new BuildException("You do not have a test description"));
            processedMethod.setDescription("None");
        }

        List<XStep> steps = new ArrayList<>();
        methodSet.setAssertComments(new ArrayList<>());
        BlockStmt content = methodDeclaration.getBody();//Getting the method's content
        List<Comment> comments = content.getAllContainedComments();//Retrieving all comments from the content

        //For whatever reasons comments may not be in order so let's sort them
        Collections.sort(
                comments,
                (ca, cb) -> ca.getBeginLine() - cb.getBeginLine());

        //Checks that there are actual comments before the first instruction
        if (comments.isEmpty()
                || comments.get(0).getBeginLine() > content.getChildrenNodes().get(0).getBeginLine()) {
            methodSet.getErrorManager().add(new BuildException("The first line of the method must be a comment describing the initial state."));
        }

        //Initializing the Initial state to an empty chain of characters
        String initialstate = "";

        //Creating an arraylist where we'll put the lines where the steps start
        List<List<String>> stepsLines = new ArrayList<>();

        for (Comment c : comments) //Looping through all the retrieved comments to get the initial state and the lines where steps start
        {
            if (c.getEndLine() <= content.getChildrenNodes().get(0).getBeginLine())//Getting all the lines before the first instruction, which we'll consider as the initial state
            {
                initialstate += c.getContent() + "\n";
            }
            if (c.getContent().contains("Step:"))//Gets the line number for each step, which will be used later as markers
            {
                List<String> r = new ArrayList<>();
                r.add(Integer.toString(c.getBeginLine()));
                r.add(c.getContent().substring(c.getContent().indexOf("Step:") + 5));
                stepsLines.add(r);
            }

            if (c.getContent().startsWith("Expected result:")) {
                c.setContent(c.getContent().substring(16).replace("\"", "'"));
                methodSet.getAssertComments().add(c);
            }
        }
        //Sorting the comments by their line number
        Collections.sort(stepsLines, (o1, o2) -> o1.get(0).compareTo(o2.get(0)));

        //Checking the first step wasn't mixed up with the initial state, if so, delete that part
        if (initialstate.contains("Step:")) {
            initialstate = initialstate.substring(0, initialstate.indexOf("Step:"));
        }
        if (initialstate.replace(" ", "").isEmpty()) {
            methodSet.getErrorManager().add(new BuildException("You need to add an initial state "));
        }

        if (initialstate.contains("Initial state:")) {
            initialstate = initialstate.substring(initialstate.indexOf("Initial state:") + "Initial state:".length());
        }

        //Finally adding that initial state to the method
        processedMethod.setInitialState(initialstate.replace("\"", "'"));

        //Putting all comments on an arraylist to solve the problem concerning assert() comments
        methodSet.setAllComments(methodDeclaration.getAllContainedComments());

        new TestMethodVisitor().visit(methodDeclaration, methodSet);
        //Checking that there are actual assert() calls
        if (methodSet.getAssertComments().isEmpty()) {
            methodSet.getErrorManager().add(new BuildException("Can't find any calls to an assert method in your " + methodDeclaration.getName() + " @Test method."));
        }

        if (stepsLines.size() < 1) {
            methodSet.getErrorManager().add(new BuildException("Cannot find steps"));
        } else {
            //Now that the step lines are in order, we'll take all comments which line number is under the line number of the following step, then removing it from the list
            for (int i = 0; i < stepsLines.size() - 1; i++) {
                XStep step = new XStep();
                ArrayList<String> expectedResult = new ArrayList<>();

                Iterator<Comment> iter = methodSet.getAssertComments().iterator();

                while (iter.hasNext()) {
                    Comment c = iter.next();
                    if (c.getEndLine() < Integer.parseInt(stepsLines.get(i + 1).get(0))) {
                        expectedResult.add(c.getContent());
                        iter.remove();
                    }
                }
                step.setStep(stepsLines.get(i).get(1));
                step.setExpectedResult(expectedResult);
                steps.add(step); //Adding the XStep(Step + its expected results) to the list of steps
            }

            //Need to put the comments on the last step 
            XStep mstep = new XStep();
            List<String> expectedResult = new ArrayList<>();
            for (Comment c : methodSet.getAssertComments()) {
                expectedResult.add(c.getContent().replace("\"", "'"));
            }
            mstep.setStep(stepsLines.get(stepsLines.size() - 1).get(1));
            mstep.setExpectedResult(expectedResult);

            steps.add(mstep);//Adding it
        }

        //Adding the XSteps to the method, adding it's name retrieved from the methodDeclaration, and adding it to the list of methods.
        processedMethod.setSteps(steps);
        processedMethod.setName(methodDeclaration.getName());
        methodSet.getTestMethods().add(processedMethod);

    }

    public List<Method> getMethods() {
        return methodSet.getTestMethods();
    }

    public List<MethodDeclaration> getAllmymethods() {
        return methodSet.getAllMethods();
    }

    public void setAllmymethods(List<MethodDeclaration> allmymethods) {
        methodSet.setAllMethods(allmymethods);
    }

}
