package dk.via.ahlang;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static dk.via.ahlang.TokenKind.ERROR;
import static dk.via.ahlang.TokenKind.getTokensWithSpelling;

public class Scanner {
    private SourceFile source;

    private char currentChar;
    private final StringBuffer currentSpelling = new StringBuffer();
    private final Map<String, TokenKind> stringTokens;
    private final Set<String> tokensWithMoreThanOneChar = new HashSet<>();

    public Scanner(SourceFile source) {
        this.source = source;

        currentChar = source.getSource();
        stringTokens = getTokensWithSpelling();

        //Finding tokens with more than one char from here
        for (String token : stringTokens.keySet()) {
            if (token.length() > 1) {
                tokensWithMoreThanOneChar.add(token);
            }
        }
    }

    private void appendChar() {
        currentSpelling.append(currentChar);
        currentChar = source.getSource();
        skipWhitespace();
    }

    private void skipWhitespace() {
        while (currentChar == ' ' || currentChar == '\n' || currentChar == '\r' || currentChar == '\t') {
            currentChar = source.getSource();
        }
    }

    private static boolean isLetter(char c)
    {
        //There is a Character.isLetter(), but we might want to use the danish symbols for something else.
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }


    private static boolean isDigit(char c)
    {
        return Character.isDigit(c);
    }

    public Token scan() {
        currentSpelling.delete(0, currentSpelling.length());
        TokenKind kind = scanToken();
        return new Token(kind, currentSpelling.toString());
    }

    private TokenKind scanToken() {
        if(stringTokens.containsKey("" + currentChar) || isStringInLongerTokens("" + currentChar)) {
            return TokensWithDeterminedGrammar();
        } else if (isDigit(currentChar)) {
            return NumericToken();
        } else if(isLetter(currentChar)) {
            return IdentifierToken();
        } else if (stringTokens.get(Character.toString(currentChar)) == TokenKind.EOT) {
            return TokenKind.EOT;
        }

        System.out.println(stringTokens.keySet());
        throw new IllegalArgumentException(String.valueOf(ERROR) + " Current Char: " + currentChar + " Current Spelling: " + currentSpelling);
    }

    private TokenKind TokensWithDeterminedGrammar() {
        appendChar();
        while(isStringInLongerTokens(currentSpelling.toString() + currentChar)) {
            appendChar();
        }
        return stringTokens.containsKey(currentSpelling.toString()) ? stringTokens.get(currentSpelling.toString()) : IdentifierToken();
        //If it ends up not being a determined one, it can only be Identifier, since we don't allow tings to start with numbers.
    }

    private TokenKind NumericToken() {
        while (isDigit(currentChar)) {
            appendChar();
        }
        return TokenKind.NUMERIC;
    }

    private TokenKind IdentifierToken() {
        appendChar();
        while(isLetter(currentChar) || isDigit(currentChar)) {
            if(stringTokens.containsKey(currentSpelling.toString())) {
                return stringTokens.get(currentSpelling.toString());
            }
            appendChar();
        }
        return TokenKind.IDENTIFIER;
    }

    private boolean isStringInLongerTokens(String check) {
        for(String token : tokensWithMoreThanOneChar) {
            if(token.startsWith(check)) {
                return true;
            }
        }
        return false;
    }
}
