package dk.via.ahlang.ast;

public class Assignment extends Statement {
    public Identifier identifier;
    public Expression expression;
    public Assignment(Identifier identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }
}
