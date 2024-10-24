package dk.via.ahlang.ast;

public class Assignment extends Statement {
    public Identifier identifier;
    public Expression expression;
    public Numeric index;

    public Assignment(Identifier identifier, Expression expression, Numeric index) {
        this.identifier = identifier;
        this.expression = expression;
        this.index = index;
    }
}
