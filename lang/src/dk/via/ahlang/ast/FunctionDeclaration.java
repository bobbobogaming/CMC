package dk.via.ahlang.ast;

import java.util.List;

public class FunctionDeclaration extends Statement{
    public Identifier identifier;
    public List<Parameter> parameterList;
    public Type returnType;
    public Block block;

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
