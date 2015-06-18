package org.dhatim.jtestdoc.visitors;

import org.apache.tools.ant.BuildException;
import org.dhatim.jtestdoc.utilities.MethodSet;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class TestMethodVisitor extends VoidVisitorAdapter<MethodSet>
{
	MethodSet m;
	private void recursiveSearch(Node n)
	{
		for(MethodDeclaration mm:m.getAllmymethods())
		{
			if(((MethodCallExpr)((ExpressionStmt)n).getExpression()).getName().equals(mm.getName()))
			{
				visit(mm,null);
			}
		}
	}
	
	
	@Override
	public void visit(ExpressionStmt n,MethodSet m)
	{
		this.m = m;
		if(n.getExpression() instanceof MethodCallExpr 
			 )
		{
			if(((MethodCallExpr)(n.getExpression())).getName().contains("assert"))
			{
				if(n.getComment()!=null)
					m.getAssertComments().add(n.getComment());
				else
				{
					boolean found = false;
					for(Comment c:m.getAllComments())
					{
						if(c.getEndLine()+1==n.getBeginLine())
						{
							c.setContent(c.getContent().replace("\"", "'"));
							m.getAssertComments().add(c);
							found = true;
						}
						else if(c.getContent().startsWith("Expected result:"))
						{
							c.setContent(c.getContent().substring(16).replace("\"", "'"));
							if(!m.getAssertComments().contains(c))
							{
								m.getAssertComments().add(c);
							}
						}
					}
					if(!found)
						m.getErrorManager().add(new BuildException("You have undocumented asserts line "+n.getBeginLine()));
				}
			}
			else recursiveSearch(n);
		}
		
	}
	
	
	@Override
	public void visit(BlockComment n, MethodSet a)
	{
		System.out.println(n.getComment().getContent()+"\n\n");
	}
}
