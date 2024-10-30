package dk.via.ahlang.ast;

import java.util.ArrayList;
import java.util.List;

public class StatementCol implements AST {
    public List<Statement> statements = new ArrayList<>();

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitStatementCol(this, arg);
    }
}
