/*
 * 04.10.2016 Minor edit
 * 11.11.2009 New package structure
 * 26.10.2006 Original version (based on Watt&Brown)
 */
 
package dk.via.jpe.intlang;


import dk.via.jpe.intlang.ast.*;

import TAM.*;

import java.io.*;


public class Encoder
	implements Visitor
{
	private int nextAdr = Machine.CB;
	private int currentLevel = 0;
	
	
	private void emit( int op, int n, int r, int d )
	{
		if( n > 255 ) {
			System.out.println( "Operand too long" );
			n = 255;
		}
		
		Instruction instr = new Instruction();
		instr.op = op;
		instr.n = n;
		instr.r = r;
		instr.d = d;
		
		if( nextAdr >= Machine.PB )
			System.out.println( "Program too large" );
		else
			Machine.code[nextAdr++] = instr;
	}
	
	private void patch( int adr, int d )
	{
		Machine.code[adr].d = d;
	}
	
	private int displayRegister( int currentLevel, int entityLevel )
	{
		if( entityLevel == 0 )
			return Machine.SBr;
		else if( currentLevel - entityLevel <= 6 )
			return Machine.LBr + currentLevel - entityLevel;
		else {
			System.out.println( "Accessing across to many levels" );
			return Machine.L6r;
		}
	}
	
	
	public void saveTargetProgram( String fileName )
	{
		try {
			DataOutputStream out = new DataOutputStream( new FileOutputStream( fileName ) );
			
			for( int i = Machine.CB; i < nextAdr; ++i )
				Machine.code[i].write( out );
			                      
			out.close();
		} catch( Exception ex ) {
			ex.printStackTrace();
			System.out.println( "Trouble writing " + fileName );
		}
	}
	
	
	public void encode( Program p )
	{
		p.visit( this, null );
	}

	
	public Object visitProgram( Program p, Object arg )
	{
		currentLevel = 0;
		
		p.block.visit( this, new Address() );
		
		emit( Machine.HALTop, 0, 0, 0 );
		
		return null;
	}
	
	
	public Object visitBlock( Block b, Object arg )
	{
		int before = nextAdr;
		emit( Machine.JUMPop, 0, Machine.CB, 0 );
		
		int size = ((Integer) b.decs.visit( this, arg )).intValue();
		
		patch( before, nextAdr );
		
		if( size > 0 )
			emit( Machine.PUSHop, 0, 0, size );
		
		b.stats.visit( this, null );
		
		return size;
	}
	
	
	public Object visitDeclarations( Declarations d, Object arg )
	{
		int startDisplacement = ((Address) arg).displacement;
		
		for( Declaration dec : d.dec )
			arg = dec.visit( this, arg );
			
		Address adr = (Address) arg;
		int size = adr.displacement - startDisplacement;
			
		return new Integer( size );
	}
	
	
	public Object visitVariableDeclaration( VariableDeclaration v, Object arg )
	{
		v.adr = (Address) arg;
		
		return new Address( (Address) arg, 1 );
	}
	
	
	public Object visitFunctionDeclaration( FunctionDeclaration f, Object arg )
	{
		f.address = new Address( currentLevel, nextAdr );
		
		++currentLevel;
		
		Address adr = new Address( (Address) arg );
		
		int size = ((Integer) f.params.visit( this, adr ) ).intValue();
		f.params.visit( this, new Address( adr, -size ) );

		f.block.visit( this, new Address( adr, Machine.linkDataSize ) );
		f.retExp.visit( this, new Boolean( true ) );
		
		emit( Machine.RETURNop, 1, 0, size );

		currentLevel--;
		
		return arg;
	}
	
	
	public Object visitStatements( Statements s, Object arg )
	{
		for( Statement stat : s.stat )
			stat.visit( this, null );
			
		return null;
	}
	
	
	public Object visitExpressionStatement( ExpressionStatement e, Object arg )
	{
		e.exp.visit( this, new Boolean( false ) );
		
		return null;
	}
	
	
	public Object visitIfStatement( IfStatement i, Object arg )
	{
		i.exp.visit( this, new Boolean( true ) );
		
		int jump1Adr = nextAdr;
		emit( Machine.JUMPIFop, 0, Machine.CBr, 0 );
		
		i.thenPart.visit( this, null );
		
		int jump2Adr = nextAdr;
		emit( Machine.JUMPop, 0, Machine.CBr, 0 );
		
		patch( jump1Adr, nextAdr );
		
		i.elsePart.visit( this, null );
		
		patch( jump2Adr, nextAdr );
		
		return null;
	}
	
	
	public Object visitWhileStatement( WhileStatement w, Object arg )
	{
		int startAdr = nextAdr;
		
		w.exp.visit( this, new Boolean( true ) );
		
		int jumpAdr = nextAdr;
		emit( Machine.JUMPIFop, 0, Machine.CBr, 0 );
		
		w.stats.visit( this, null );
		
		emit( Machine.JUMPop, 0, Machine.CBr, startAdr );
		patch( jumpAdr, nextAdr );
		
		return null;
	}
	
	
	public Object visitSayStatement( SayStatement s, Object arg )
	{
		s.exp.visit( this, new Boolean( true ) );
		
		emit( Machine.CALLop, 0, Machine.PBr, Machine.putintDisplacement );
		emit( Machine.CALLop, 0, Machine.PBr, Machine.puteolDisplacement );
		
		return null;
	}
	
	
	public Object visitBinaryExpression( BinaryExpression b, Object arg )
	{
		boolean valueNeeded = ((Boolean) arg).booleanValue();
		
		String op = (String) b.operator.visit( this, null );
		
		if( op.equals( ":=" ) ) {
			Address adr = (Address) b.operand1.visit( this, new Boolean( false ) );
			b.operand2.visit( this, new Boolean( true ) );
			
			int register = displayRegister( currentLevel, adr.level );
			emit( Machine.STOREop, 1, register, adr.displacement );
			
			if( valueNeeded )
				emit( Machine.LOADop, 1, register, adr.displacement );
		} else {
			b.operand1.visit( this, arg );
			b.operand2.visit( this, arg );
			
			if( valueNeeded )
				if( op.equals( "+" ) )
					emit( Machine.CALLop, 0, Machine.PBr, Machine.addDisplacement );
				else if( op.equals( "-" ) )
					emit( Machine.CALLop, 0, Machine.PBr, Machine.subDisplacement );
				else if( op.equals( "*" ) )
					emit( Machine.CALLop, 0, Machine.PBr, Machine.multDisplacement );
				else if( op.equals( "/" ) )
					emit( Machine.CALLop, 0, Machine.PBr, Machine.divDisplacement );
				else if( op.equals( "%" ) )
					emit( Machine.CALLop, 0, Machine.PBr, Machine.modDisplacement );
		}
		
		return null;
	}
	
	
	public Object visitVarExpression( VarExpression v, Object arg )
	{
		boolean valueNeeded = ((Boolean) arg).booleanValue();
		
		Address adr = v.decl.adr;
		int register = displayRegister( currentLevel, adr.level );
		
		if( valueNeeded )
			emit( Machine.LOADop, 1, register, adr.displacement );
			
		return adr;
	}
	
	
	public Object visitCallExpression( CallExpression c, Object arg )
	{
		boolean valueNeeded = ((Boolean) arg).booleanValue();
		
		c.args.visit( this, null );
		
		Address adr = c.decl.address;
		int register = displayRegister( currentLevel, adr.level );
		
		emit( Machine.CALLop, register, Machine.CB, adr.displacement );
		
		if( !valueNeeded )
			emit( Machine.POPop, 0, 0, 1 );
		
		return null;
	}
	
	
	public Object visitUnaryExpression( UnaryExpression u, Object arg )
	{
		boolean valueNeeded = ((Boolean) arg).booleanValue();
		
		String op = (String) u.operator.visit( this, null );
		u.operand.visit( this, arg );
		
		if( valueNeeded && op.equals( "-") )
			emit( Machine.CALLop, 0, Machine.PBr, Machine.negDisplacement );
		
		return null;
	}
	
	
	public Object visitIntLitExpression( IntLitExpression i, Object arg )
	{
		boolean valueNeeded = ((Boolean) arg).booleanValue();
		
		Integer lit = (Integer) i.literal.visit( this, null );
		
		if( valueNeeded )
			emit( Machine.LOADLop, 1, 0, lit.intValue() );
			
		return null;
	}
	
	
	public Object visitExpList( ExpList e, Object arg )
	{
		for( Expression exp: e.exp )
			exp.visit( this, new Boolean( true ) );
			
		return null;
	}
	
	
	public Object visitIdentifier( Identifier i, Object arg )
	{
		return null;
	}
	
	
	public Object visitIntegerLiteral( IntegerLiteral i, Object arg )
	{
		return new Integer( i.spelling );
	}
	
	
	public Object visitOperator( Operator o, Object arg )
	{
		return o.spelling;
	}	
}