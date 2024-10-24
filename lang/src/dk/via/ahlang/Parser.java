package dk.via.ahlang;

import dk.via.ahlang.ast.*;
import dk.via.jpe.intlang.ast.Declaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static dk.via.ahlang.TokenKind.*;

public class Parser {
	private final dk.via.ahlang.Scanner scan;

	private Token currentTerminal;

	public Parser(Scanner scan)
	{
		this.scan = scan;

		currentTerminal = scan.scan();
	}

	public Program parseProgram()
	{
		StatementCol statementCol = parseStatementCollection();

		if( currentTerminal.kind != EOT )
			System.out.println( "Tokens found after end of program" );

		return new Program(statementCol);
	}

	private StatementCol parseStatementCollection() {
		HashSet<TokenKind> tokens = new HashSet<>(
			Arrays.asList(IF, WHILE,TYPE, FUNCTIONKEY,IDENTIFIER,CONSOLEIN, CONSOLEOUT));
		StatementCol statementCol = new StatementCol();

		while (tokens.contains(currentTerminal.kind)) {
			statementCol.statements.add(parseStatement());
		}

		return statementCol;
	}

	private Block parseBlock() {
		accept(COLON);
		StatementCol collection = parseStatementCollection();
		Expression returnExpression = null;
		if (currentTerminal.kind == RETURN) {
			accept(RETURN);
			returnExpression = parseMathExpr();
			accept(SEMICOLON);
		}
		accept(END);
		return new Block(collection, returnExpression);
	}

	private Statement parseStatement() {
		switch (currentTerminal.kind) {
			case IF, WHILE -> {
				return parseControlFlow();
			}
			case TYPE, FUNCTIONKEY -> {
				return parseDeclaration();
			}
			case IDENTIFIER -> {
				return parseAssignment();
			}
			case CONSOLEIN -> {
				return parseConsoleIn();
			}
			case CONSOLEOUT -> {
				return parseConsoleOut();
			}
		}
		throw new RuntimeException("Unreachable");
	}

	private Statement parseAssignment() {
		Identifier identifier = new Identifier(currentTerminal.spelling);
		accept(IDENTIFIER);
		Numeric index = null;
		if (currentTerminal.kind == OPENBRACKET) {
			accept(OPENBRACKET);
			index = new Numeric(currentTerminal.spelling);
			accept(NUMERIC);
			accept(CLOSEBRACKET);
		}
		accept(ASSIGN);
		Expression expression = parseMathExpr();
		accept(SEMICOLON);
		return new Assignment(identifier, expression, index);
	}
	private List<Expression> parseFunctionArguments() {
		accept(LEFTPARAN);
		List<Expression> list = parseArgumentList();
		accept(RIGHTPARAN);
		return list;
	}

	private List<Expression> parseArgumentList() {
		List<Expression> list = new ArrayList<>();
		list.add(parseMathExpr());
		parseArgumentListTail(list);
		return list;
	}

	private void parseArgumentListTail(List<Expression> list) {
		if (currentTerminal.kind == COMMA) {
			accept(COMMA);
			list.add(parseMathExpr());
			parseArgumentListTail(list);
		}
	}

	private Statement parseControlFlow() {
		if (currentTerminal.kind == WHILE) {
			accept(WHILE);
			Expression condition = parseComparison();
			Block block = parseBlock();
			return new WhileStatement(condition, block);
		}
		accept(IF);
		Expression condition = parseComparison();
		Block block = parseBlock();
		Block elseBlock = null;
		if (currentTerminal.kind == ELSE) {
			accept(ELSE);
			elseBlock = parseBlock();
		}
		return new IfStatement(condition, block, elseBlock);
	}

	private Expression parseComparison() {
		Expression left = parseMathExpr();
		Operator operator = new Operator(currentTerminal.spelling);
		accept(COMPARISON);
		Expression right = parseMathExpr();
		return new BinaryExpression(left, operator, right);
	}

	private Expression parseMathExpr() {
		Expression left = parseSecondMathExpr();
		if (currentTerminal.kind == ADDSUBOP) {
			Operator operator = new Operator(currentTerminal.spelling);
			accept(ADDSUBOP);
			Expression right = parseMathExpr();
			return new BinaryExpression(left, operator, right);
		}
		return left;
	}

	private Expression parseSecondMathExpr() {
		Expression left = parsePrimaryMathExpr();
		if (currentTerminal.kind == MULDIVOP) {
			Operator operator = new Operator(currentTerminal.spelling);
			accept(MULDIVOP);
			Expression right = parseSecondMathExpr();
			return new BinaryExpression(left, operator, right);
		}
		return left;
	}

	private Expression parsePrimaryMathExpr() {
		if (currentTerminal.kind == NUMERIC) {
			Numeric numeric = new Numeric(currentTerminal.spelling);
			accept(NUMERIC);
			return numeric;
		}
		else if (currentTerminal.kind == STRING) {
			StringAH string = new StringAH(currentTerminal.spelling);
			accept(STRING);
			return string;
		}
		else if (currentTerminal.kind == IDENTIFIER) {
			Identifier identifier = new Identifier(currentTerminal.spelling);
			accept(IDENTIFIER);
			if (currentTerminal.kind == LEFTPARAN) {
				return new FunctionCall(identifier, parseFunctionArguments());
			}
			return identifier;
		}
		else if (currentTerminal.kind == LEFTPARAN) {
			accept(LEFTPARAN);
			Expression expr = parseMathExpr();
			accept(RIGHTPARAN);
			return expr;
		}
		throw new RuntimeException("Unreachable");
	}

	private Statement parseDeclaration() {
		if (currentTerminal.kind == TYPE) {
			Type type = new Type(currentTerminal.spelling);
			accept(TYPE);
			Identifier identifier = new Identifier(currentTerminal.spelling);
			accept(IDENTIFIER);
			if (currentTerminal.kind == OPENBRACKET) {
				accept(OPENBRACKET);
				if (currentTerminal.kind == CLOSEBRACKET) {
					accept(CLOSEBRACKET);
					accept(ASSIGN);
					accept(OPENCURLYBRACE);
					List<Expression> list = parseArgumentList();
					accept(CLOSECURLYBRACE);
					accept(SEMICOLON);
					return new VariableDeclaration(type, identifier, list);
				}
				Numeric numeric = new Numeric(currentTerminal.spelling);
				accept(NUMERIC);
				accept(CLOSEBRACKET);
				accept(SEMICOLON);
				return new VariableDeclaration(type, identifier, numeric);
			}

			Expression initialValue = null;
			if (currentTerminal.kind == ASSIGN) {
				accept(ASSIGN);
				initialValue = parseMathExpr();
			}
			accept(SEMICOLON);
			return new VariableDeclaration(type, identifier, initialValue);
		} else {
			accept(FUNCTIONKEY);
			Identifier identifier = new Identifier(currentTerminal.spelling);
			accept(IDENTIFIER);
			accept(LEFTPARAN);
			List<Parameter> parameters = parseParamList();
			accept(RIGHTPARAN);
			Type returnType = null;
			if (currentTerminal.kind == TYPE) { //TODO: Dont remember if we wanted to declare return type, but we have return as optional
				returnType = new Type(currentTerminal.spelling);
				accept(TYPE);
			}
			Block block = parseBlock();
			return new FunctionDeclaration(identifier, parameters,returnType, block);
		}
	}

	private void parseArrayDeclaration() {
		accept(OPENBRACKET);
		if(currentTerminal.kind == NUMERIC) {
			Numeric numeric = new Numeric(currentTerminal.spelling);
			accept(NUMERIC);
			accept(CLOSEBRACKET);
			accept(SEMICOLON);
		}
		accept(CLOSEBRACKET);
		accept(ASSIGN);
		accept(OPENCURLYBRACE);
		List<Expression> list = parseArgumentList();
		accept(CLOSECURLYBRACE);
		accept(SEMICOLON);
	}

	private List<Parameter> parseParamList() {
		List<Parameter> list = new ArrayList<>();
		Type type = new Type(currentTerminal.spelling);
		accept(TYPE);
		Identifier identifier = new Identifier(currentTerminal.spelling);
		accept(IDENTIFIER);
		list.add(new Parameter(type, identifier));
		parseParamListTail(list);
		return list;
	}

	private void parseParamListTail(List<Parameter> list) { //TODO: Think some of these methods can be done in a do while
		if (currentTerminal.kind == COMMA) {
			accept(COMMA);
			Type type = new Type(currentTerminal.spelling);
			accept(TYPE);
			Identifier identifier = new Identifier(currentTerminal.spelling);
			accept(IDENTIFIER);
			list.add(new Parameter(type, identifier));
			parseParamListTail(list);
		}
	}

	private Statement parseConsoleOut() {
		accept(CONSOLEOUT);
		Expression expression = parseMathExpr();
		accept(SEMICOLON);
		return new ConsoleOutDeclaration(expression);
	}

	private Statement parseConsoleIn() {
		accept(CONSOLEIN);
		Expression expression = parseMathExpr();
		accept(SEMICOLON);
		return new ConsoleInDeclaration(expression);
	}

	private void accept( TokenKind expected )
	{
		if( currentTerminal.kind == expected )
			currentTerminal = scan.scan();
		else
			System.out.println( "Expected token of kind " + expected );
	}
}
