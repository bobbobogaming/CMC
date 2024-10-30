package dk.via.ahlang.ast;

public class Numeric extends Terminal{
    public Numeric(String spelling) {
        super(spelling);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitNumeric(this, arg);
    }
}
