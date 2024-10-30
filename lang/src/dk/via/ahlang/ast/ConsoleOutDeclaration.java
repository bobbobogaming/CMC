package dk.via.ahlang.ast;

public class ConsoleOutDeclaration extends Statement{
    public Expression expression;

    public ConsoleOutDeclaration(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitConsoleOutDeclaration(this, arg);
    }
}
