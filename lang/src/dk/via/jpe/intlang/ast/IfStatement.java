/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 22.10.2006 visit()
 * 29.09.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


public class IfStatement
	extends Statement
{
	public Expression exp;
	public Statements thenPart;
	public Statements elsePart;
	
	
	public IfStatement( Expression exp, Statements thenPart, Statements elsePart )
	{
		this.exp = exp;
		this.thenPart = thenPart;
		this.elsePart = elsePart;
	}
	
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitIfStatement( this, arg );
	}
}