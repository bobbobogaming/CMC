package dk.via.ahlang;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static dk.via.ahlang.TokenKind.*;

public class Scanner {
    private final SourceFile source;
    private char currentChar;
    private final StringBuilder currentSpelling = new StringBuilder();
    private final Map<String, TokenKind> stringTokens;
    private final Set<String> multiCharTokens = new HashSet<>();

    public Scanner(SourceFile source) {
        this.source = source;
        currentChar = source.getSource();
        skipWhitespace();
        stringTokens = getTokensWithSpelling();

        //Finding tokens with more than one char from here
        for (String token : stringTokens.keySet()) {
            if (token.length() > 1) {
                multiCharTokens.add(token);
            }
        }
    }

    private void  appendChar() {
        currentSpelling.append(currentChar);
        currentChar = source.getSource();
        skipWhitespace();
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(currentChar)) {
            currentChar = source.getSource();
        }
    }

    public Token scan() {
        currentSpelling.delete(0, currentSpelling.length());
        TokenKind kind = scanToken();
        return new Token(kind, currentSpelling.toString());
    }

    private TokenKind scanToken() {
        String currentCharStr = Character.toString(currentChar);
        if(stringTokens.containsKey(currentCharStr) || isStringInLongerTokens(currentCharStr)) {
            return determinedTokens();
        } else if (Character.isDigit(currentChar)) {
            return numericToken();
        } else if(Character.isLetter(currentChar)) {
            return identifierToken();
        }

        System.out.println(stringTokens.keySet());
        throw new IllegalArgumentException(ERROR + " Current Char: " + currentChar + " Current Spelling: " + currentSpelling);
    }

    private TokenKind determinedTokens() {
        do {
            appendChar();
            if(stringTokens.get(currentSpelling.toString()) == STRING) {return handleStringToken();}
            if(stringTokens.get(currentSpelling.toString()) == COMMENTSTART) {return removeComment();}
        } while (isStringInLongerTokens(currentSpelling.toString() + currentChar));
        return stringTokens.containsKey(currentSpelling.toString()) ? stringTokens.get(currentSpelling.toString()) : identifierToken();
        //If it ends up not being a determined one, it can only be Identifier, since we don't allow tings to start with numbers.
    }

    private TokenKind handleStringToken() {
        while(stringTokens.get(Character.toString(currentChar)) != STRING) {
            if(stringTokens.get(Character.toString(currentChar)) == EOT) {
                throw new IllegalArgumentException(ERROR + " Current Char: " + currentChar + " Current Spelling: " + currentSpelling + " Missing end indicator for string token");
            }
            appendChar();
        } //This isn't very flex in case we want to change the symbol to more than one, but i wanna get a move on
        appendChar();
        return STRING;
    }

    private TokenKind removeComment() { //Not very happy with how I've built this up
        currentSpelling.delete(0, currentSpelling.length());
        StringBuilder comment = new StringBuilder();
        do {
            comment.append(source.getSource());
            if (stringTokens.get(comment.toString()) == EOT) {
                throw new IllegalArgumentException(ERROR + " Current Char: " + currentChar + " Current Spelling: " + currentSpelling + " Doesn't end comment section off");
            }
            if (!isStringInLongerTokens(comment.toString())) {
                comment.setLength(0);
            }
        } while (stringTokens.get(comment.toString()) != COMMENTEND);
        currentChar = source.getSource();
        skipWhitespace();
        return scanToken();
    }

    private TokenKind identifierToken() {
        do {
            appendChar();
            if(stringTokens.containsKey(currentSpelling.toString())) {
                return stringTokens.get(currentSpelling.toString());
            }
        } while(Character.isLetter(currentChar) || Character.isDigit(currentChar));
        return IDENTIFIER;
    }

    private TokenKind numericToken() {
        do {
            appendChar();
        } while (Character.isDigit(currentChar));
        return NUMERIC;
    }

    private boolean isStringInLongerTokens(String check) {
        return multiCharTokens.stream().anyMatch(token -> token.startsWith(check));
    }
}