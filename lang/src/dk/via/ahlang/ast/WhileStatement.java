package dk.via.ahlang.ast;

public class WhileStatement extends Statement {
    public Expression condition;
    public Block block;

    public WhileStatement(Expression condition, Block block) {
        this.condition = condition;
        this.block = block;
    }
}
