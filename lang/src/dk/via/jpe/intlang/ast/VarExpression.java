/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 27.10.2006 decl added
 * 22.10.2006 visit()
 * 01.10.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


public class VarExpression
	extends Expression
{
	public Identifier name;
	public VariableDeclaration decl;
	
	
	public VarExpression( Identifier name )
	{
		this.name = name;
	}
	
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitVarExpression( this, arg );
	}
}