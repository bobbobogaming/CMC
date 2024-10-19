package dk.via.ahlang.ast;

public class IfStatement extends Statement {
    public Expression condition;
    public Block thenBlock;
    public Block elseBlock;

    public IfStatement(Expression condition, Block thenBlock, Block elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }
}