/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 22.10.2006 visit()
 * 29.09.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


import java.util.*;


public class Statements
	extends AST
{
	public Vector<Statement> stat = new Vector<Statement>();
	
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitStatements( this, arg );
	}
}