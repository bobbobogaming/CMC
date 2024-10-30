package dk.via.ahlang.ast;

public class Type extends Terminal{ //Todo: Type should not inherent the expression which is in terminal, so yea
    public Type(String spelling) {
        super(spelling);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitType(this, arg);
    }
}
