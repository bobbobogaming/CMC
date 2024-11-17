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
        String idSpelling = (String) assignment.identifier.visit(this, null);

        DeclarationInterface declaration = (DeclarationInterface) idTable.retrieve(idSpelling);
        if (declaration == null) {
            throw new RuntimeException("\"" + idSpelling + "\" must be declared before it’s used");
        }

        if (assignment.index != null && declaration instanceof VariableDeclaration variable) {

        }

        assignment.expression.visit(this, declaration.getType());

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
        String idSpelling = (String) functionDeclaration.identifier.visit(this, null);

        Statement existingVariableDeclaration = idTable.retrieveFromCurrentScope(idSpelling);
        if (existingVariableDeclaration != null) {
            throw new RuntimeException("Can't declare \"" + idSpelling + "\" in the same scope twice!");
        } else {
            idTable.enter(idSpelling, functionDeclaration);
        }
        idTable.openScope();
        for (Parameter parameter : functionDeclaration.parameterList) {
            parameter.visit(this, null);
        }

        functionDeclaration.block.visit(this, functionDeclaration.returnType);

        idTable.closeScope();

        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        ifStatement.condition.visit(this, null);
        idTable.openScope();
        ifStatement.thenBlock.visit(this, null);
        idTable.closeScope();
        if (ifStatement.elseBlock != null) {
            idTable.openScope();
            ifStatement.elseBlock.visit(this, null);
            idTable.closeScope();
        }

        return null;
    }

    @Override
    public Object visitVariableDeclaration(VariableDeclaration variableDeclaration, Object arg) {
        String idSpelling = (String) variableDeclaration.identifier.visit(this, null);

        Statement existingVariableDeclaration = idTable.retrieveFromCurrentScope(idSpelling);
        if (existingVariableDeclaration != null) {
            throw new RuntimeException("Can't declare \"" + idSpelling + "\" in the same scope twice!");
        } else {
            idTable.enter(idSpelling, variableDeclaration);
        }

        if (variableDeclaration.initialValue != null) {
            variableDeclaration.initialValue.visit(this, variableDeclaration.type);
        } else if (variableDeclaration.initialValues!= null) {
            for (Expression expression : variableDeclaration.initialValues) {
                expression.visit(this, variableDeclaration.type);
            }
        }

        return null;
    }

    @Override
    public Object visitWhileStatement(WhileStatement whileStatement, Object arg) {
        whileStatement.condition.visit(this, null);
        idTable.openScope();
        whileStatement.block.visit(this, null);
        idTable.closeScope();
        return null;
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        block.statements.visit(this, null);

        if (block.returnExpression != null) {
            block.returnExpression.visit(this, arg);
        }

        return null;
    }

    @Override
    public Object visitParameter(Parameter parameter, Object arg) {
        String idSpelling = (String) parameter.identifier.visit(this, null);

        Statement existingVariableDeclaration = idTable.retrieveFromCurrentScope(idSpelling);
        if (existingVariableDeclaration != null) {
            throw new RuntimeException("Can't declare \"" + idSpelling + "\" in the same scope twice!");
        } else {
            idTable.enter(idSpelling, parameter);
        }
        return null;
    }

    @Override
    public Object visitFunctionCall(FunctionCall functionCall, Object arg) {
        String idSpelling = (String) functionCall.identifier.visit(this, null);

        Statement functionStatement = idTable.retrieve(idSpelling);
        if(functionStatement == null) {
            throw new RuntimeException("No func by name \"" + idSpelling + "\" has been declared");
        }
        if(!(functionStatement instanceof FunctionDeclaration)) {
            throw new RuntimeException("Identifier \"" + idSpelling + "\" doesn't belong to a function!");
        }
        FunctionDeclaration functionDeclaration = (FunctionDeclaration) functionStatement;
        if(functionDeclaration.parameterList.size() - functionCall.arguments.size() != 0) {
            throw new RuntimeException("Parameter and/or argument size doesn't align for call \"" + idSpelling + "\"");
        }

        for (int i = 0; i < functionDeclaration.parameterList.size(); i++) {
            functionCall.arguments.get(i).visit(this, functionDeclaration.parameterList.get(i).type);
        }

        if(arg instanceof Type type) {
            checkTypeVisit(type, functionDeclaration.returnType);
        }
        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) {
        Type type = null;
        if (arg != null) {
            type = (Type) arg;
        }

        binaryExpression.left.visit(this, type);
        binaryExpression.operator.visit(this, type);
        binaryExpression.right.visit(this, type);

        return null;
    }

    @Override
    public Object visitIdentifier(Identifier identifier, Object arg) {
        if (arg instanceof Type type) {
            Statement statement = idTable.retrieve(identifier.spelling);
            if (statement == null) {
                throw new RuntimeException("Identifier cas not found: \"" + identifier.spelling + "\"");
            }
            DeclarationInterface declarationInterface = (DeclarationInterface) idTable.retrieve(identifier.spelling);
            checkTypeVisit(declarationInterface.getType(), type);
        }
        return identifier.spelling;
    }

    @Override
    public Object visitNumeric(Numeric numeric, Object arg) {
        if (arg instanceof Type type) {
            checkTypeVisit(new Type("#"), type);
        }
        return numeric.spelling;
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {

        if (arg instanceof Type type && type.spelling.equals("§")) {
            if (operator.spelling.equals("+")) {
                return null;
            }
            throw new RuntimeException("Strings can only use operation: \"+\" Is: \" Not " + operator.spelling + "\"");
        }
        return operator.spelling;
    }

    @Override
    public Object visitStringAH(StringAH stringAH, Object arg) {
        if (arg instanceof Type type) {
            checkTypeVisit(new Type("§"), type);
        }
        return stringAH.spelling;
    }

    /*private void checkTypeVisit(TokenKind wantedReturn, Type returnType) {
        if (wantedReturn.hasSpelling(returnType.spelling))
            return;
        throw new RuntimeException("Not expected type: Expected: \"" + wantedReturn + "\" Is: \"" + returnType.spelling + "\"");
    }*/

    private void checkTypeVisit(Type type, Type returnType) {
        if(type.spelling.equals(returnType.spelling)) {
            return;
        }
        throw new RuntimeException("Wrong type: Expected: \"" + returnType.spelling + "\" Is: \"" + type.spelling + "\"");
    }

    @Override
    public Object visitType(Type type, Object arg) {
        return type.spelling;
    }
}
