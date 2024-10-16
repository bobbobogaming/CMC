package dk.via.ahlang;

import java.util.Arrays;
import java.util.HashSet;

import static dk.via.ahlang.TokenKind.*;

public class Parser {
	private final dk.via.ahlang.Scanner scan;


	private Token currentTerminal;


	public Parser( Scanner scan )
	{
		this.scan = scan;

		currentTerminal = scan.scan();
	}

	public void parseProgram()
	{
		parseStatementCollection();

		if( currentTerminal.kind != EOT )
			System.out.println( "Tokens found after end of program" );
	}

	private void parseStatementCollection() {
		HashSet<TokenKind> tokens = new HashSet<>(
			Arrays.asList(IF, WHILE,TYPE, FUNCTIONKEY,IDENTIFIER,CONSOLEIN, CONSOLEOUT));

		while (tokens.contains(currentTerminal.kind)) {
			parseStatement();
		}
	}

	private void parseBlock() {
		accept(COLON);
		parseStatementCollection();
		if (currentTerminal.kind == RETURN) {
			accept(RETURN);
			parseMathExpr();
			accept(SEMICOLON);
		}
		accept(END);
	}

	private void parseStatement() {
		switch (currentTerminal.kind) {
			case IF, WHILE -> parseControlFlow();
			case TYPE, FUNCTIONKEY -> parseDeclaration();
			case IDENTIFIER -> parseAssignment();
			case CONSOLEIN -> parseConsoleIn();
			case CONSOLEOUT -> parseConsoleOut();
		}
	}

	private void parseAssignment() {
		accept(IDENTIFIER);
		accept(ASSIGN);
		parseMathExpr();
		accept(SEMICOLON);
	}
	private void parseFunctionArguments() {
		accept(LEFTPARAN);
		parseArgumentList();
		accept(RIGHTPARAN);
	}

	private void parseArgumentList() {
		parseMathExpr();
		parseArgumentListTail();
	}

	private void parseArgumentListTail() {
		if (currentTerminal.kind == COMMA) {
			accept(COMMA);
			parseMathExpr();
			parseArgumentListTail();
		}
	}

	private void parseControlFlow() {
		if (currentTerminal.kind == WHILE) {
			accept(WHILE);
			parseComparison();
			parseBlock();
			return;
		}
		accept(IF);
		parseComparison();
		parseBlock();
		if (currentTerminal.kind == ELSE) {
			accept(ELSE);
			parseBlock();
		}
	}

	private void parseComparison() {
		parseMathExpr();
		accept(COMPARISON);
		parseMathExpr();
	}

	private void parseMathExpr() {
		parseSecondMathExpr();
		if (currentTerminal.kind == ADDSUBOP) {
			accept(ADDSUBOP);
			parseMathExpr();
		}
	}

	private void parseSecondMathExpr() {
		parsePrimaryMathExpr();
		if (currentTerminal.kind == MULDIVOP) {
			accept(MULDIVOP);
			parseSecondMathExpr();
		}
	}

	private void parsePrimaryMathExpr() {
		if (currentTerminal.kind == NUMERIC) {
			accept(NUMERIC);
		}
		else if (currentTerminal.kind == STRING) {
			accept(STRING);
		}
		else if (currentTerminal.kind == IDENTIFIER) {
			accept(IDENTIFIER);
			if (currentTerminal.kind == LEFTPARAN) {
				parseFunctionArguments();
			}
		}
		else if (currentTerminal.kind == LEFTPARAN) {
			accept(LEFTPARAN);
			parseMathExpr();
			accept(RIGHTPARAN);
		}
	}

	private void parseDeclaration() {
		if (currentTerminal.kind == TYPE) {
			parseType();
			accept(IDENTIFIER);
			if (currentTerminal.kind == ASSIGN) {
				accept(ASSIGN);
				parseMathExpr();
			}
			accept(SEMICOLON);
		} else {
			accept(FUNCTIONKEY);
			accept(IDENTIFIER);
			accept(LEFTPARAN);
			parseParamList();
			accept(RIGHTPARAN);
			parseType();
			parseBlock();
		}
	}

	private void parseParamList() {
		parseType();
		accept(IDENTIFIER);
		parseParamListTail();
	}

	private void parseParamListTail() {
		if (currentTerminal.kind == COMMA) {
			accept(COMMA);
			parseType();
			accept(IDENTIFIER);
			parseParamListTail();
		}
	}

	private void parseConsoleOut() {
		accept(CONSOLEOUT);
		parseMathExpr();
		accept(SEMICOLON);
	}

	private void parseConsoleIn() {
		accept(CONSOLEIN);
		parseMathExpr();
		accept(SEMICOLON);
	}

	private void parseType() {
		accept(TYPE);
	}

	private void accept( TokenKind expected )
	{
		if( currentTerminal.kind == expected )
			currentTerminal = scan.scan();
		else
			System.out.println( "Expected token of kind " + expected );
	}
}
