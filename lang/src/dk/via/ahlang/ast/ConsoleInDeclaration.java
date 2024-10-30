package dk.via.ahlang.ast;

public class ConsoleInDeclaration extends Statement{
    public Expression expression;

    public ConsoleInDeclaration(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitConsoleInDeclaration(this, arg);
    }
}
