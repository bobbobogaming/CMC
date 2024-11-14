/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 29.10.2009 New package structure
 * 22.10.2006 visit()
 * 01.10.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


public class UnaryExpression
	extends Expression
{
	public Operator operator;
	public Expression operand;
	
	
	public UnaryExpression( Operator operator, Expression operand )
	{
		this.operator = operator;
		this.operand = operand;
	}
	
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitUnaryExpression( this, arg );
	}
}