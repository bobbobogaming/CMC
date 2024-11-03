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
        consoleInDeclaration.expression.visit(this, null);
        return null;
    }

    @Override
    public Object visitConsoleOutDeclaration(ConsoleOutDeclaration consoleOutDeclaration, Object arg) {
        consoleOutDeclaration.expression.visit(this, null);
        return null;
    }

    @Override
    public Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg) {
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        ifStatement.condition.visit(this, null);
        ifStatement.thenBlock.visit(this, null);
        if (ifStatement.elseBlock != null) {
            ifStatement.elseBlock.visit(this, null);
        }

        return null;
    }

    @Override
    public Object visitVariableDeclaration(VariableDeclaration variableDeclaration, Object arg) {
        String idSpelling = (String) variableDeclaration.identifier.visit(this, null);

        Statement existingVariableDeclaration = idTable.retrieveFromCurrentScope(idSpelling);
        if (existingVariableDeclaration != null) {
            System.out.println("Can't declare \"" + idSpelling + "\" in the same scope twice!");
        } else {
            idTable.enter(idSpelling, variableDeclaration);
        }

        return null;
    }

    @Override
    public Object visitWhileStatement(WhileStatement whileStatement, Object arg) {
        whileStatement.condition.visit(this, null);
        whileStatement.block.visit(this, null);

        return null;
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        idTable.openScope();
        block.statements.visit(this, null);
        idTable.closeScope();

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
        return identifier.spelling;
    }

    @Override
    public Object visitNumeric(Numeric numeric, Object arg) {
        return numeric.spelling;
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        return operator.spelling;
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
