/*
 * 25.09.2019 TokenKind enum introduced
 * 13.09.2016 IParse gone, IScanner gone, minor editing
 * 11.10.2010 Small fix
 * 01.10.2010 Renamed
 * 21.10.2009 New folder structure
 * 24.10.2006 params in function declaration empty Declarations instead of null
 * 23.10.2006 IdList replaced by Declarations for function parameters
 * 22.10.2006 Error in parsePrimary fixed
 * 01.10.2006 AST introduced
 * 28.09.2006 Original version (based on Watt&Brown)
 */
 
package dk.via.jpe.intlang;


import static dk.via.jpe.intlang.TokenKind.*;

import dk.via.jpe.intlang.ast.*;


public class ParserAST
{
	private Scanner scan;
	
	
	private Token currentTerminal;
	
	
	public ParserAST( Scanner scan )
	{
		this.scan = scan;
		
		currentTerminal = scan.scan();
	}
	
	
	public Program parseProgram()
	{
		Block block = parseBlock();
		
		if( currentTerminal.kind != EOT )
			System.out.println( "Tokens found after end of program" );
			
		return new Program( block );
	}
	
	
	private Block parseBlock()
	{
		accept( DECLARE );
		Declarations decs = parseDeclarations();
		accept( DO );
		Statements stats = parseStatements();
		accept( OD );
		
		return new Block( decs, stats );
	}
	
	
	private Declarations parseDeclarations()
	{
		Declarations decs = new Declarations();
		
		while( currentTerminal.kind == VAR ||
		       currentTerminal.kind == FUNC )
			decs.dec.add( parseOneDeclaration() );
			
		return decs;
	}
	
	
	private Declaration parseOneDeclaration()
	{
		switch( currentTerminal.kind ) {
			case VAR:
				accept( VAR );
				Identifier id = parseIdentifier();
				accept( SEMICOLON );
				
				return new VariableDeclaration( id );
				
			case FUNC:
				accept( FUNC );
				Identifier name = parseIdentifier();
				accept( LEFTPARAN );
				
				Declarations params = null;
				if( currentTerminal.kind == IDENTIFIER )
					params = parseIdList();
				else
					params = new Declarations();
					
				accept( RIGHTPARAN );
				Block block = parseBlock();
				accept( RETURN );
				Expression retExp = parseExpression();
				accept( SEMICOLON );
				
				return new FunctionDeclaration( name, params, block, retExp );
				
			default:
				System.out.println( "var or func expected" );
				return null;
		}
	}
	
	
	private Declarations parseIdList()
	{
		Declarations list = new Declarations();
		
		list.dec.add( new VariableDeclaration( parseIdentifier() ) );
		
		while( currentTerminal.kind == COMMA ) {
			accept( COMMA );
			list.dec.add( new VariableDeclaration( parseIdentifier() ) );
		}
		
		return list;
	}
	
	
	private Statements parseStatements()
	{
		Statements stats = new Statements();
		
		while( currentTerminal.kind == IDENTIFIER ||
		       currentTerminal.kind == OPERATOR ||
		       currentTerminal.kind == INTEGERLITERAL ||
		       currentTerminal.kind == LEFTPARAN ||
		       currentTerminal.kind == IF ||
		       currentTerminal.kind == WHILE ||
		       currentTerminal.kind == SAY )
			stats.stat.add( parseOneStatement() );
			
		return stats;
	}
	
	
	private Statement parseOneStatement()
	{
		switch( currentTerminal.kind ) {
			case IDENTIFIER:
			case INTEGERLITERAL:
			case OPERATOR:
			case LEFTPARAN:
				Expression exp = parseExpression();
				accept( SEMICOLON );
				
				return new ExpressionStatement( exp );
				
			case IF:
				accept( IF );
				Expression ifExp = parseExpression();
				accept( THEN );
				Statements thenPart = parseStatements();
				
				Statements elsePart = null;
				if( currentTerminal.kind == ELSE ) {
					accept( ELSE );
					elsePart = parseStatements();
				}
				
				accept( FI );
				accept( SEMICOLON );
				
				return new IfStatement( ifExp, thenPart, elsePart );
				
			case WHILE:
				accept( WHILE );
				Expression whileExp = parseExpression();
				accept( DO );
				Statements stats = parseStatements();
				accept( OD );
				accept( SEMICOLON );
				
				return new WhileStatement( whileExp, stats );
				
			case SAY:
				accept( SAY );
				Expression sayExp = parseExpression();
				accept( SEMICOLON );
				
				return new SayStatement( sayExp );
				
			default:
				System.out.println( "Error in statement" );
				return null;
		}
	}
	
	
	private Expression parseExpression()
	{
		Expression res = parsePrimary();
		while( currentTerminal.kind == OPERATOR ) {
			Operator op = parseOperator();
			Expression tmp = parsePrimary();
			
			res = new BinaryExpression( op, res, tmp );
		}
		
	return res;
	}
	
	
	private Expression parsePrimary()
	{
		switch( currentTerminal.kind ) {
			case IDENTIFIER:
				Identifier name = parseIdentifier();
				
				if( currentTerminal.kind == LEFTPARAN ) {
					accept( LEFTPARAN );
					
					ExpList args;
					
					if( currentTerminal.kind == IDENTIFIER ||
					    currentTerminal.kind == INTEGERLITERAL ||
					    currentTerminal.kind == OPERATOR ||
					    currentTerminal.kind == LEFTPARAN )
					    
						args = parseExpressionList();
					else
						args = new ExpList();
						
					
					accept( RIGHTPARAN );
					
					return new CallExpression( name, args );
				} else
					return new VarExpression( name );
				
			case INTEGERLITERAL:
				IntegerLiteral lit = parseIntegerLiteral();
				return new IntLitExpression( lit );
				
			case OPERATOR:
				Operator op = parseOperator();
				Expression exp = parsePrimary();
				return new UnaryExpression( op, exp );
				
			case LEFTPARAN:
				accept( LEFTPARAN );
				Expression res = parseExpression();
				accept( RIGHTPARAN );
				return res;
				
			default:
				System.out.println( "Error in primary" );
				return null;
		}
	}
	
	
	private ExpList parseExpressionList()
	{
		ExpList exps = new ExpList();
		
		exps.exp.add( parseExpression() );
		while( currentTerminal.kind == COMMA ) {
			accept( COMMA );
			exps.exp.add( parseExpression() );
		}
		
		return exps;
	}
	
	
	private Identifier parseIdentifier()
	{
		if( currentTerminal.kind == IDENTIFIER ) {
			Identifier res = new Identifier( currentTerminal.spelling );
			currentTerminal = scan.scan();
			
			return res;
		} else {
			System.out.println( "Identifier expected" );
			
			return new Identifier( "???" );
		}
	}
	
	
	private IntegerLiteral parseIntegerLiteral()
	{
		if( currentTerminal.kind == INTEGERLITERAL ) {
			IntegerLiteral res = new IntegerLiteral( currentTerminal.spelling );
			currentTerminal = scan.scan();
			
			return res;
		} else {
			System.out.println( "Integer literal expected" );
			
			return new IntegerLiteral( "???" );
		}
	}
	
	
	private Operator parseOperator()
	{
		if( currentTerminal.kind == OPERATOR ) {
			Operator res = new Operator( currentTerminal.spelling );
			currentTerminal = scan.scan();
			
			return res;
		} else {
			System.out.println( "Operator expected" );
			
			return new Operator( "???" );
		}
	}
	
	
	private void accept( TokenKind expected )
	{
		if( currentTerminal.kind == expected )
			currentTerminal = scan.scan();
		else
			System.out.println( "Expected token of kind " + expected );
	}
}