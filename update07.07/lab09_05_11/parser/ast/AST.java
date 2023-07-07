package lab09_05_11.parser.ast;

import lab09_05_11.visitors.Visitor;
import lab09_05_11.visitors.execution.VectorValue;

public interface AST {
	<T> T accept(Visitor<T> visitor);
}
