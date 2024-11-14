/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 22.10.2006 visit(), decl
 * 29.09.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


public class Identifier
	extends Terminal
{
	public Declaration decl;
	
	
	public Identifier( String spelling )
	{
		this.spelling = spelling;
	}
	
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitIdentifier( this, arg );
	}
}