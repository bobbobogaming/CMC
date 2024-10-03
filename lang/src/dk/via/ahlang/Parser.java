package dk.via.ahlang;

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
		while (currentTerminal.kind != EOT) {
			parseStatement();
		}
	}

	private void parseBlock() { //TODO finish
		accept(COLON);
		parseStatementCollection();
		if (currentTerminal.kind == RETURN) {
			accept(RETURN);
			accept(IDENTIFIER);
			accept(SEMICOLON);
		}
		accept(END);
	}

	private void parseStatement() { //TODO finish
		switch (currentTerminal.kind) {
			case IF, WHILE -> parseControlFlow();
		}
	}

	private void parseControlFlow() { //TODO finish
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
		if (currentTerminal.kind == NUMERIC) {
			accept(NUMERIC);
		} else {
			accept(IDENTIFIER);
		}
		accept(OPERATOR);
		if (currentTerminal.kind == NUMERIC) {
			accept(NUMERIC);
		} else {
			accept(IDENTIFIER);
		}
	}

	private void parseDeclaration() { //TODO check if finished
		if (currentTerminal.kind == TYPE) {
			parseType();
			accept(IDENTIFIER);
			if (currentTerminal.spelling.equals("=")) {
				accept(OPERATOR);
				if (currentTerminal.kind == NUMERIC) { // TODO consider if we aren't missing string literals
					accept(NUMERIC);
				} else {
					accept(IDENTIFIER);
				}
			}
			accept(SEMICOLON);
		} else {
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

	private void parseConsoleOut() { // TODO Finish. everything between the `CONSOLEOUT` and `SEMICOLON` should maybe be a method like jans `parsePrimary()`
		accept(CONSOLEOUT);
		accept(IDENTIFIER);
		accept(SEMICOLON);
	}

	private void parseConsoleIn() {
		accept(CONSOLEIN);
		accept(IDENTIFIER);
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
