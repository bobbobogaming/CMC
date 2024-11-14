/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 22.10.2006 visit()
 * 01.01.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


import java.util.*;


public class ExpList
	extends AST
{
	public Vector<Expression> exp = new Vector<Expression>();
	
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitExpList( this, arg );
	}
}