package dk.via.ahlang.ast;

public class Operator extends Terminal{
    public Operator(String spelling) {
        super(spelling);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitOperator(this, arg);
    }
}
