package lab09_05_11.parser.ast;

import static java.util.Objects.requireNonNull;

import lab09_05_11.visitors.Visitor;

public class Block implements Stmt {
	private final StmtSeq stmtSeq;

	public Block(StmtSeq stmtSeq) {
		this.stmtSeq = requireNonNull(stmtSeq);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + stmtSeq + ")";
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitBlock(stmtSeq);
	}

}
