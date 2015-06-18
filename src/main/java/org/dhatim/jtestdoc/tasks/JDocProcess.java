package org.dhatim.jtestdoc.tasks;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.dhatim.jtestdoc.beans.File;
import org.dhatim.jtestdoc.utilities.JTDAG;
import org.dhatim.jtestdoc.utilities.MethodSet;
import org.dhatim.jtestdoc.visitors.TestTagVisitor;

import org.dhatim.jtestdoc.tasks.JDocProcess;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JDocProcess extends Task 
{
	ArrayList<File> files = new ArrayList<File>(); //This is the list with all the processed files
	String destination; //This is the place where the generated documentation will be exported
	public ArrayList<MethodDeclaration> allmymethods;
	FileSet f = new FileSet();//This is the fileset with the files to be processed
	boolean blocking;//This defines if the task should throw errors(blocking the task at the first one) or simply generate warnings(and create a doc file even if there are warnings)
	

	public void execute() throws BuildException 
	{
		if(destination==null)//If the destination is null, set it to a default location documentation.html
			this.destination = "documentation.html";

		 //Parsing files
		for(Resource mfi:f)//Looping through the list of of files
		{
			MethodSet m = new MethodSet(blocking);
			allmymethods = new ArrayList<MethodDeclaration>();
			
			File d=new File();
			FileInputStream in=null;
			CompilationUnit cu = null;
				
			try {
				in = new FileInputStream(mfi.toString());
				cu = JavaParser.parse(in);
			} catch (ParseException|FileNotFoundException e) {
				e.printStackTrace();
			}
			
			TestTagVisitor a = new TestTagVisitor();
	        new MethodVisitor().visit(cu, this);
	        a.setAllmymethods(allmymethods);
			
	        a.visit(cu,m);
			d.setMethods(a.getMethods());
			d.setName(mfi.getName());
			if(!d.getMethods().isEmpty())
				files.add(d);
		}

		JTDAG doc = new JTDAG(files, destination); //Create a JTestDocumentation with the files
		doc.export(); //Export it
	
	}
	
	public void addFileSet(FileSet f)
	{
		this.f = f;
	}
	public void setBlocking(String s)
	{
		blocking = Boolean.parseBoolean(s);
	}
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	private static class MethodVisitor extends VoidVisitorAdapter<Object> {

	        @Override
	        public void visit(MethodDeclaration n, Object arg) {
	        	JDocProcess s = (JDocProcess) arg;
	        	s.allmymethods.add(n);
	        }
	}
}
