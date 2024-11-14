/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 22.10.2006 visit()
 * 29.09.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


public class Block
	extends AST
{
	public Declarations decs;
	public Statements stats;
	
	
	public Block( Declarations decs, Statements stats )
	{
		this.decs = decs;
		this.stats = stats;
	}
	
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitBlock( this, arg );
	}
}