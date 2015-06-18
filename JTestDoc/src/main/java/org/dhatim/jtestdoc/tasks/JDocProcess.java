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
	ArrayList<File> files = new ArrayList<File>();
	String destination;
	public ArrayList<MethodDeclaration> allmymethods;
	FileSet f = new FileSet();
	boolean blocking;
	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void execute() throws BuildException 
	{
		if(destination==null)
			this.destination = "documentation.html";
		try{
		 //Parsing files
		for(Resource mfi:f)
		{
			MethodSet m = new MethodSet(blocking);
			allmymethods = new ArrayList<MethodDeclaration>();
			File d=new File();
			FileInputStream in=null;
			 CompilationUnit cu = null;
				try {
					in = new FileInputStream(mfi.toString());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
			try {
				cu = JavaParser.parse(in);
			} catch (ParseException e) {
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

		JTDAG doc = new JTDAG(files, destination);
		doc.export();
		}catch(Exception e)
		{
        	e.printStackTrace();
		}
	}
	
	public void addFileSet(FileSet f)
	{
		this.f = f;
	}
	public void setBlocking(String s)
	{
		blocking = Boolean.parseBoolean(s);
	}
	
	private static class MethodVisitor extends VoidVisitorAdapter<Object> {

	        @Override
	        public void visit(MethodDeclaration n, Object arg) {
	        	JDocProcess s = (JDocProcess) arg;
	        	s.allmymethods.add(n);
	        }
	}
}
