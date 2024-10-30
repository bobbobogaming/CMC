package dk.via.ahlang.ast;

public class StringAH extends Terminal{
    public StringAH(String spelling) {
        super(spelling);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitStringAH(this, arg);
    }
}
