package dk.via.ahlang.ast;

import java.util.List;

public class FunctionDeclaration extends Statement{
    Identifier identifier;
    List<Parameter> parameterList;
    Type returnType;
    Block block;

    public FunctionDeclaration(Identifier identifier, List<Parameter> parameterList, Type returnType, Block block) {
        this.identifier = identifier;
        this.parameterList = parameterList;
        this.returnType = returnType;
        this.block = block;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitFunctionDeclaration(this, arg);
    }
}
