package lab09_05_11.parser.ast;

import lab09_05_11.visitors.Visitor;
import lab09_05_11.visitors.execution.VectorValue;

public class Snd extends UnaryOp {

	public Snd(Exp exp) {
		super(exp);
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitSnd(exp);
	}
}
