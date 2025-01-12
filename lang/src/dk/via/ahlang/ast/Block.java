package dk.via.ahlang.ast;

public class Block implements AST {
    public StatementCol statements;
    public Expression returnExpression;

    public Block(StatementCol statements, Expression expression) {
        this.statements = statements;
        this.returnExpression = expression;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitBlock(this, arg);
    }
}
