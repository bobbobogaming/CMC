package dk.via.ahlang.ast;

public class VariableDeclaration extends Statement{
    Type type;
    Identifier identifier;
    Expression initialValue;

    public VariableDeclaration(Type type, Identifier identifier, Expression initialValue) {
        this.type = type;
        this.identifier = identifier;
        this.initialValue = initialValue;
    }
}
