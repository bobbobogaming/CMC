package dk.via.ahlang.ast;

public class BinaryExpression extends Expression {
    public Expression left;
    public Operator operator;
    public Expression right;

    public BinaryExpression(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
