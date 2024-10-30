package dk.via.ahlang.ast;

public class Identifier extends Terminal {
    public Identifier(String spelling) {
        super(spelling);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitIdentifier(this, arg);
    }
}
