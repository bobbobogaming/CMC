package dk.via.ahlang.ast;

public class Parameter extends Statement implements DeclarationInterface {
    public Type type;
    public Identifier identifier;

    public Parameter(Type type, Identifier identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitParameter(this, arg);
    }

    @Override
    public Type getType() {
        return type;
    }
}
