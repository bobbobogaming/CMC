package dk.via.ahlang.ast;

import java.util.List;

public class VariableDeclaration extends Statement{
    Type type;
    Identifier identifier;
    Expression initialValue;
    List<Expression> initialValues;
    Numeric size;

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
}