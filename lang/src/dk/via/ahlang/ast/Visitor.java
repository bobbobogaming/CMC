package dk.via.ahlang.ast;

public interface Visitor {
    Object visitProgram(Program program, Object arg);
    Object visitBlock(Block block, Object arg);
    Object visitVariableDeclaration(VariableDeclaration variableDeclaration, Object arg);
    Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg);
    Object visitConsoleInDeclaration(ConsoleInDeclaration consoleInDeclaration, Object arg);
    Object visitConsoleOutDeclaration(ConsoleOutDeclaration consoleOutDeclaration, Object arg);
    Object visitStatementCol(StatementCol statementCol, Object arg);
    Object visitAssignment(Assignment assignment, Object arg);
    Object visitIfStatement(IfStatement ifStatement, Object arg);
    Object visitWhileStatement(WhileStatement whileStatement, Object arg);
    Object visitParameter(Parameter parameter, Object arg);
    Object visitFunctionCall(FunctionCall functionCall, Object arg);
    Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg);
    Object visitIdentifier(Identifier identifier, Object arg);
    Object visitNumeric(Numeric numeric, Object arg);
    Object visitOperator(Operator operator, Object arg);
    Object visitStringAH(StringAH stringAH, Object arg);
    Object visitType(Type type, Object arg);
}
