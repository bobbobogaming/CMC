/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 28.10.2006 address
 * 23.10.2006 IdList replaced by Declarations for function parameters
 * 22.10.2006 visit()
 * 29.09.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


import dk.via.jpe.intlang.*;


public class FunctionDeclaration
	extends Declaration
{
	public Identifier name;
	public Declarations params;
	public Block block;
	public Expression retExp;
	
	public Address address;
	
	
	public FunctionDeclaration( Identifier name, Declarations params,
	                            Block block, Expression retExp )
	{
		this.name = name;
		this.params = params;
		this.block = block;
		this.retExp = retExp;
	}
	
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitFunctionDeclaration( this, arg );
	}
}