package dk.via.ahlang.ast;

import java.util.List;

public class VariableDeclaration extends Statement implements DeclarationInterface{
    public Type type;
    public Identifier identifier;
    public Expression initialValue;
    public List<Expression> initialValues;
    public Numeric size;

    public VariableDeclaration(Type type, Identifier identifier, Expression initialValue) {
        this.type = type;
        this.identifier = identifier;
        this.initialValue = initialValue;
    }

    public VariableDeclaration(Type type, Identifier identifier, List<Expression> initialValues) {
        this.type = type;
        this.identifier = identifier;
        this.initialValues = initialValues;
    }

    public VariableDeclaration(Type type, Identifier identifier, Numeric size) {
        this.type = type;
        this.identifier = identifier;
        this.size = size;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitVariableDeclaration(this, arg);
    }

    @Override
    public Type getType() {
        return type;
    }
}
