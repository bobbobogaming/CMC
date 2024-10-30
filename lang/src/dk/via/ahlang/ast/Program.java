package dk.via.ahlang.ast;

public class Program implements AST{
    public StatementCol collection;

    public Program(StatementCol collection) {
        this.collection = collection;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitProgram(this, arg);
    }
}
