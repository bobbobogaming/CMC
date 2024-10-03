package dk.via.ahlang;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TokenKind {
    RETURN("->"),
    IDENTIFIER,
    NUMERIC,
    NUMERICCOMMA("."),
    OPERATOR("+","-","*","/", "%", "<", ">", "="),
    COMMA( "," ),
    SEMICOLON( ";" ),
    COLON(":"),
    LEFTPARAN( "(" ),
    RIGHTPARAN( ")" ),
    IF("?"),
    ELSE("¤"),
    WHILE("!"),
    TYPE("½", "#", "§"),
    FUNCTIONKEY("<->"),
    CONSOLEIN("<<"),
    CONSOLEOUT(">>"),
    END(".."),
    COMMENTSTART("/*"),
    COMMENTEND("*/"),
    STRING("'"),
    ERROR,
    EOT(Character.toString(0)),
    ;

    private List<String> spellings;

    TokenKind() {}
    TokenKind(String... s) {this.spellings = Arrays.asList(s);}

    public static Map<String, TokenKind> getTokensWithSpelling() {
        Map<String, TokenKind> map = new HashMap<>();
        for (TokenKind kind : TokenKind.values()) {
            if(kind.spellings != null) {
                for (String s : kind.spellings) {
                    map.put(s, kind);
                }
            }
        }
        return map;
    }
}
