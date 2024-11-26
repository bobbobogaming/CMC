package dk.via.ahlang;

import TAM.Instruction;
import TAM.Machine;
import dk.via.ahlang.ast.*;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class Encoder implements Visitor {
    private int nextAdr = Machine.CB;
    private int currentLevel = 0;

    private void emit(int op, int n, int r, int d)
    {
        if( n > 255 ) {
            System.out.println("Operand too long");
            n = 255;
        }

        Instruction instr = new Instruction();
        instr.op = op;
        instr.n = n;
        instr.r = r;
        instr.d = d;

        if(nextAdr >= Machine.PB)
            System.out.println("Program too large");
        else
            Machine.code[nextAdr++] = instr;
    }

    private void patch(int adr, int d)
    {
        Machine.code[adr].d = d;
    }

    private int displayRegister(int currentLevel, int entityLevel)
    {
        if(entityLevel == 0)
            return Machine.SBr;
        else if( currentLevel - entityLevel <= 6 )
            return Machine.LBr + currentLevel - entityLevel;
        else {
            System.out.println("Accessing across to many levels");
            return Machine.L6r;
        }
    }


    public void saveTargetProgram(String fileName)
    {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));

            for(int i = Machine.CB; i < nextAdr; ++i)
                Machine.code[i].write(out);

            out.close();
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Trouble writing " + fileName);
        }
    }


    public void encode(Program p)
    {
        p.visit( this, null );
    }

    @Override
    public Object visitProgram(Program program, Object arg) {
        currentLevel = 0;

        program.collection.visit(this, new Address());

        emit(Machine.HALTop, 0, 0, 0);

        return null;
    }

    @Override
    public Object visitStatementCol(StatementCol statementCol, Object arg) {

        for (Statement statement : statementCol.statements) {
            statement.visit(this, arg);
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
        Type t = (Type)consoleOutDeclaration.expression.visit(this, true);
        if (t == null) {
            System.out.println(((Address)arg).displacement);
            //emit( Machine.LOADop, 1, register, adr.displacement );
            return null;
        }
        if (t.spelling.equals("#")) {
            emit(Machine.CALLop, 0, Machine.PBr, Machine.putintDisplacement);
            emit(Machine.CALLop, 0, Machine.PBr, Machine.puteolDisplacement);
        }
        if (t.spelling.equals("§")) {
            int jumpAdr = nextAdr;
            emit(Machine.CALLop, 0, Machine.PBr, Machine.putDisplacement);
            emit(Machine.JUMPIFop,1,Machine.CBr, jumpAdr);
            emit(Machine.CALLop, 0, Machine.PBr, Machine.puteolDisplacement);
        }

        return null;
    }

    @Override
    public Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg) {
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        ifStatement.condition.visit(this,true);

        int jump1Adr = nextAdr;
        emit(Machine.JUMPIFop, 0, Machine.CBr,0);

        ifStatement.thenBlock.visit(this,null);

        int jump2Adr = nextAdr;
        emit(Machine.JUMPop, 0 ,Machine.CBr,0);

        patch(jump1Adr,nextAdr);
        ifStatement.elseBlock.visit(this,null);

        patch(jump2Adr,nextAdr);
        return null;
    }

    @Override
    public Object visitVariableDeclaration(VariableDeclaration variableDeclaration, Object arg) { // not working right
        variableDeclaration.address = (Address) arg;
        if (variableDeclaration.initialValue == null) {
            emit(Machine.PUSHop,0,0,1);
        } else {
            variableDeclaration.initialValue.visit(this,true);
        }
        return new Address((Address) arg,1);
    }

    @Override
    public Object visitWhileStatement(WhileStatement whileStatement, Object arg) {
        return null;
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        currentLevel++;
        block.statements.visit(this,null);
        currentLevel--;
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
        String op = binaryExpression.operator.spelling;

        int oldAdr = nextAdr;
        Type t = (Type) binaryExpression.left.visit(this, true);
        if (t.spelling.equals("§") && op.equals("+")) {
            nextAdr = oldAdr;
            binaryExpression.right.visit(this, true);
            oldAdr = nextAdr;
            binaryExpression.left.visit(this, true);
            patch(oldAdr,1);
        } else {
            binaryExpression.right.visit(this, true);
        }

        if (t.spelling.equals("#")) {
            switch (op) {
                case "+" ->
                    emit(Machine.CALLop, 0, Machine.PBr, Machine.addDisplacement);
                case "-" ->
                    emit(Machine.CALLop, 0, Machine.PBr, Machine.subDisplacement);
                case "*" ->
                    emit(Machine.CALLop, 0, Machine.PBr, Machine.multDisplacement);
                case "/" ->
                    emit(Machine.CALLop, 0, Machine.PBr, Machine.divDisplacement);
                case ">" ->
                    emit(Machine.CALLop, 0, Machine.PBr, Machine.gtDisplacement);
                case "<" ->
                    emit(Machine.CALLop, 0, Machine.PBr, Machine.ltDisplacement);
                case "==" -> {
                    emit( Machine.LOADLop, 1, 0, 1 );
                    emit(Machine.CALLop, 0, Machine.PBr, Machine.eqDisplacement);
                }
            }
        }

        return t;
    }

    @Override
    public Object visitIdentifier(Identifier identifier, Object arg) {
        return null;
    }

    @Override
    public Object visitNumeric(Numeric numeric, Object arg) {
        boolean valueNeeded = ((Boolean) arg).booleanValue();

        Integer lit = Integer.valueOf(numeric.spelling);

        if( valueNeeded )
            emit( Machine.LOADLop, 1, 0, lit.intValue() );

        return new Type("#");
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        return null;
    }

    @Override
    public Object visitStringAH(StringAH stringAH, Object arg) {

        String str = stringAH.spelling;

        emit(Machine.LOADLop, 0, 0, 0);

        char[] stringAHChars = str.toCharArray();
        for (int i = stringAHChars.length - 1; i >= 0; i--) {
            if (i != stringAHChars.length - 1) {
                emit(Machine.LOADLop, 0, 0, 1);
            }
            emit( Machine.LOADLop, 1, 0, stringAHChars[i] );
        }

        return new Type("§");
    }

    @Override
    public Object visitType(Type type, Object arg) {
        return null;
    }

    @Override
    public Object visitArrayCall(ArrayCall arrayCall, Object arg) {
        return null;
    }
}
