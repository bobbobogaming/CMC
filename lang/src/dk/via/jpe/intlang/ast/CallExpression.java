/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 28.10.2006 decl
 * 22.10.2006 visit()
 * 01.10.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


public class CallExpression
	extends Expression
{
	public Identifier name;
	public ExpList args;
	
	public FunctionDeclaration decl;
	
	
	public CallExpression( Identifier name, ExpList args )
	{
		this.name = name;
		this.args = args;
	}
	
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitCallExpression( this, arg );
	}
}