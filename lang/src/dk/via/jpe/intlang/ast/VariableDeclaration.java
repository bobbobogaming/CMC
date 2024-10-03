/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 21.10.2009 New folder structure
 * 29.09.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


public class VariableDeclaration
	extends Declaration
{
	public Identifier id;
	
	
	public VariableDeclaration( Identifier id )
	{
		this.id = id;
	}
}