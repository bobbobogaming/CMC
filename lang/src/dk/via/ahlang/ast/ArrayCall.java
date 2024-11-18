package dk.via.ahlang.ast;

public class ArrayCall extends Expression {
    public Identifier identifier;
    public Numeric index;

    public ArrayCall(Identifier identifier, Numeric index) {
        this.identifier = identifier;
        this.index = index;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return null;
    }
}
