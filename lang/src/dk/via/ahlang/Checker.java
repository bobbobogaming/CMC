package dk.via.ahlang;

import dk.via.ahlang.ast.*;

public class Checker implements Visitor {
    private IdentificationTable idTable = new IdentificationTable();

    public void check(Program program) {
        program.visit(this, null);
    }

    @Override
    public Object visitProgram(Program program, Object arg) {
        idTable.openScope();

        program.collection.visit(this, null);

        idTable.closeScope();
        return null;
    }

    @Override
    public Object visitStatementCol(StatementCol statementCol, Object arg) {
        for (Statement statement : statementCol.statements) {
            statement.visit(this, null);
        }

        return null;
    }

    @Override
    public Object visitAssignment(Assignment assignment, Object arg) {
        return null;
    }

    @Override
    public Object visitConsoleInDeclaration(ConsoleInDeclaration consoleInDeclaration, Object arg) {
        return null;
    }

    @Override
    public Object visitConsoleOutDeclaration(ConsoleOutDeclaration consoleOutDeclaration, Object arg) {
        return null;
    }

    @Override
    public Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg) {
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        return null;
    }

    @Override
    public Object visitVariableDeclaration(VariableDeclaration variableDeclaration, Object arg) {
        return null;
    }

    @Override
    public Object visitWhileStatement(WhileStatement whileStatement, Object arg) {
        return null;
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        return null;
    }

    @Override
    public Object visitParameter(Parameter parameter, Object arg) {
        return null;
    }

    @Override
    public Object visitFunctionCall(FunctionCall functionCall, Object arg) {
        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitIdentifier(Identifier identifier, Object arg) {
        return null;
    }

    @Override
    public Object visitNumeric(Numeric numeric, Object arg) {
        return null;
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        return null;
    }

    @Override
    public Object visitStringAH(StringAH stringAH, Object arg) {
        return null;
    }

    @Override
    public Object visitType(Type type, Object arg) {
        return null;
    }
}
