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
			accept(IDENTIFIER);// TODO change identifier til calculation
			accept(SEMICOLON);
		}
		accept(END);
	}

	private void parseStatement() { //TODO finish
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
		parseCalculation();
		accept(SEMICOLON);
	}
	private void parseFunctionArguments() {
		accept(LEFTPARAN);
		parseArgumentList();
		accept(RIGHTPARAN);
	}

	private void parseArgumentList() {
		if (currentTerminal.kind == NUMERIC) {// TODO change identifier and numeric til calculation
			accept(NUMERIC);
			parseArgumentListTail();
		}
		else if (currentTerminal.kind == STRING) {
			accept(STRING);
			parseArgumentListTail();
		}
		else if (currentTerminal.kind == IDENTIFIER) {
			accept(IDENTIFIER);
			parseArgumentListTail();
		}
	}

	private void parseArgumentListTail() {
		if (currentTerminal.kind == COMMA) {
			accept(COMMA);
			if (currentTerminal.kind == NUMERIC) {
				accept(NUMERIC);
				parseArgumentListTail();
			}
			else if (currentTerminal.kind == STRING) {
				accept(STRING);
				parseArgumentListTail();
			}
			else {
				accept(IDENTIFIER);
				parseArgumentListTail();
			}
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
		parseCalculation();
		accept(OPERATOR);
		parseCalculation();
	}

	private void parseCalculation() {
		if (currentTerminal.kind == NUMERIC) {
			accept(NUMERIC);
		} else {
			accept(IDENTIFIER);
		}
		accept(OPERATOR);
		if (currentTerminal.kind == NUMERIC || currentTerminal.kind == IDENTIFIER) {
			parseCalculation();
		}
	}

	private void parseDeclaration() { //TODO check if finished
		if (currentTerminal.kind == TYPE) {
			parseType();
			accept(IDENTIFIER);
			if (currentTerminal.spelling.equals("=")) {
				accept(OPERATOR);
				if (currentTerminal.kind == NUMERIC) { // TODO change identifier and numeric til calculation
					accept(NUMERIC);
				}
				else if (currentTerminal.kind == IDENTIFIER) {
					accept(IDENTIFIER);
				}
				else {
					accept(STRING);
				}
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

	private void parseConsoleOut() { // TODO change identifier til calculation
		accept(CONSOLEOUT);
		accept(IDENTIFIER);
		accept(SEMICOLON);
	}

	private void parseConsoleIn() { // TODO change identifier til calculation
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
