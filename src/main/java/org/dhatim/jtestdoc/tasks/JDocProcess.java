package org.dhatim.jtestdoc.tasks;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.dhatim.jtestdoc.beans.File;
import org.dhatim.jtestdoc.utilities.JTDAG;
import org.dhatim.jtestdoc.utilities.MethodSet;
import org.dhatim.jtestdoc.visitors.TestTagVisitor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.io.IOException;
import java.util.List;

public class JDocProcess extends Task {

    private ArrayList<File> files = new ArrayList<>(); //This is the list with all the processed files
    private String destination; //This is the place where the generated documentation will be exported
    private FileSet f = new FileSet();//This is the fileset with the files to be processed
    private boolean blocking;//This defines if the task should throw errors(blocking the task at the first one) or simply generate warnings(and create a doc file even if there are warnings)

    @Override
    public void execute() throws BuildException {
        if (destination == null)//If the destination is null, set it to a default location documentation.html
        {
            this.destination = "documentation.html";
        }

        //Parsing files
        for (Resource mfi : f)//Looping through the list of of files
        {
            MethodSet methodSet = new MethodSet(blocking);
            List<MethodDeclaration> fileMethods = new ArrayList<>();

            File d = new File();
            CompilationUnit cu = null;

            try (FileInputStream in = new FileInputStream(mfi.toString())) {
                cu = JavaParser.parse(in);
            } catch (IOException | ParseException e) {
                throw new BuildException(e);
            }

            new MethodVisitor().visit(cu, fileMethods);

            TestTagVisitor testTagVisitor = new TestTagVisitor();
            testTagVisitor.setAllmymethods(fileMethods);
            testTagVisitor.visit(cu, methodSet);

            d.setMethods(testTagVisitor.getMethods());
            d.setName(mfi.getName());
            if (!d.getMethods().isEmpty()) {
                files.add(d);
            }

            JTDAG doc = new JTDAG(files, destination); //Create a JTestDocumentation with the files
            doc.export(); //Export it

        }
    }

    public void addFileSet(FileSet f) {
        this.f = f;
    }

    public void setBlocking(String s) {
        blocking = Boolean.parseBoolean(s);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    private static class MethodVisitor extends VoidVisitorAdapter<List<MethodDeclaration>> {

        @Override
        public void visit(MethodDeclaration n, List<MethodDeclaration> methods) {
            methods.add(n);
        }
    }
}
