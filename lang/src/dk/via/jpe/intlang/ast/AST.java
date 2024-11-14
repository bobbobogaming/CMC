/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 22.10.2006 visit() added
 * 29.09.2006 Original version (based on Watt&Chase)
 */
 
package dk.via.jpe.intlang.ast;


public abstract class AST
{
	public abstract Object visit( Visitor v, Object arg );
}