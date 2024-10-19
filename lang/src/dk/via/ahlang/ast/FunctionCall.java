package dk.via.ahlang.ast;

import java.util.List;

public class FunctionCall extends Expression {
    public Identifier identifier;
    public List<Expression> arguments;

    public FunctionCall(Identifier identifier, List<Expression> arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }
}
