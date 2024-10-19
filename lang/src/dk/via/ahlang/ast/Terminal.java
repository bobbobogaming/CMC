package dk.via.ahlang.ast;

public abstract class Terminal extends Expression implements AST {
    public String spelling;

    public Terminal(String spelling) {
        this.spelling = spelling;
    }
}
