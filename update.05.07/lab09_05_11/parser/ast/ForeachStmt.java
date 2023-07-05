package lab09_05_11.parser.ast;

import lab09_05_11.visitors.Visitor;

import static java.util.Objects.requireNonNull;

public class ForeachStmt implements Stmt{

    private final Variable var;
    private final Exp exp;
    private final Block block;

    public ForeachStmt(Variable var, Exp exp, Block block){
        this.var = requireNonNull(var);
        this.exp = requireNonNull(exp);
        this.block = requireNonNull(block);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(foreach " + var + " in " + exp + block.toString() + ")";
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitForeach(var, exp, block);
    }
}
