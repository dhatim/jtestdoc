package org.dhatim.jtestdoc.visitors;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.tools.ant.BuildException;
import org.dhatim.jtestdoc.utilities.MethodSet;
import org.junit.*;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.VoidType;

/**
 * The TestTagVisitorTest is a small unit test for the TestTagVisitor
 * @author Nathanaël Langlois
 * @version 1.0
 */
public class TestTagVisitorTest 
{
	MethodSet m = new MethodSet(true);
	
	/**
	 * This method tests that the visitor processes and only processes the method with the annotation @Test
	 */
	@Test
	public void testVisit() 
	{
		// We have a methodset
		 
		//Step: Mock the process method so it isn't actually called
		final AtomicBoolean processExecuted = new AtomicBoolean(false);
		TestTagVisitor visitor = new TestTagVisitor(m){
			public void process(MethodDeclaration methodDeclaration,Optional<String> markerDescription){processExecuted.set(true);}
		};
		
		//Step: Check that a method without the annotation @Test are not processed
		visitor.visit(new MarkerAnnotationExpr(new NameExpr("Override")), null);
		//The method shouldn't be processed 
		Assert.assertFalse(processExecuted.get());
		
		//Step: Check that a method with the annotation @Test is processed
		visitor.visit(new MarkerAnnotationExpr(new NameExpr("Test")), null);
		//The method should be processed
		Assert.assertTrue(processExecuted.get());
	}
	
	/**
	 * This test checks that the processing is correctly working
	 */
	@Test
	public void testProcess()
	{
		//We have the same methodset as before
		
		TestTagVisitor visitor = new TestTagVisitor(new MethodSet(true));
		
		//Step: Create a methodDeclaration without an initial state
		MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC,new VoidType(),"wala");
		BlockStmt body =  new BlockStmt();
		ArrayList<Statement> statements = new ArrayList<Statement>();
		body.setStmts(statements);
		method.setBody(body);
		try
		{
			visitor.process(method,Optional.of("nothing"));
			fail("Initial state has not been set, it should not run");
		}catch(BuildException e){
			//It should return an error since the initialstate wasn't set
			assertTrue(e.getMessage().contains("initial state"));
		}
		
		//Step:Add a comment that has a Step: to be sure it isn't mixed up with the non-existing initial state
		LineComment comment = new LineComment();
		comment.setEndLine(0);
		comment.setContent("Step: this should kinda be a step");
		BreakStmt e = new BreakStmt();
		e.setId("whatever");
		body.addOrphanComment(comment);	
		statements.add(e);
		body.setStmts(statements);
		method.setBody(body);
		
		try
		{
			visitor.process(method,Optional.of("stillnothing"));
			fail("Initial step has not been set, it should not run");
		}catch(BuildException e2){
			//An error should occur since the initial state has still not been set 
			assertTrue(e2.getMessage().contains("initial state"));
		}
		
		//Step:Adding a real initialstate as a method comment
		comment.setContent("NOW this is the initial state");
		body.addOrphanComment(comment);	
		method.setBody(body);
		
		try
		{
			visitor.process(method,Optional.of("stillnothing"));
		}catch(BuildException e2){
			//The error should not come from the nonexisting initial state
			assertFalse(e2.getMessage().contains("initial state"));
		}
		
	}
	
}

