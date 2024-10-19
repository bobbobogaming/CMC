package dk.via.ahlang.ast;

public class Parameter implements AST{
    Type type;
    Identifier identifier;

    public Parameter(Type type, Identifier identifier) {
        this.type = type;
        this.identifier = identifier;
    }
}
