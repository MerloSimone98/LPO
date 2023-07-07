package lab09_05_11.visitors.typechecking;

import static lab09_05_11.visitors.typechecking.AtomicType.*;

import lab09_05_11.environments.EnvironmentException;
import lab09_05_11.environments.GenEnvironment;
import lab09_05_11.parser.ast.*;
import lab09_05_11.visitors.Visitor;
import lab09_05_11.visitors.execution.IntValue;
import lab09_05_11.visitors.execution.InterpreterException;
import lab09_05_11.visitors.execution.VectorValue;

public class Typecheck implements Visitor<Type> {

	private final GenEnvironment<Type> env = new GenEnvironment<>();

	// useful to typecheck binary operations where operands must have the same type
	private void checkBinOp(Exp left, Exp right, Type type) {
		type.checkEqual(left.accept(this));
		type.checkEqual(right.accept(this));
	}

	// static semantics for programs; no value returned by the visitor

	@Override
	public Type visitMyLangProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undeclared variable
			throw new TypecheckerException(e);
		}
		return null;
	}

	// static semantics for statements; no value returned by the visitor

	@Override
	public Type visitAssignStmt(Variable var, Exp exp) {
		var found = env.lookup(var);
		found.checkEqual(exp.accept(this));
		return null;
	}

	@Override
	public Type visitPrintStmt(Exp exp) {
		exp.accept(this);
		return null;
	}
	@Override
	public Type visitVarStmt(Variable var, Exp exp) {
		env.dec(var, exp.accept(this));
		return null;
	}

	@Override
	public Type visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
		BOOL.checkEqual(exp.accept(this));
		thenBlock.accept(this);
		if (elseBlock != null)
			elseBlock.accept(this);
		return null;
	}

	@Override
	public Type visitBlock(StmtSeq stmtSeq) {
		env.enterScope();
		stmtSeq.accept(this);
		env.exitScope();
		return null;
	}

	@Override
	public Type visitForeach(Variable var, Exp exp, Block block) {
		VECTOR.checkEqual(exp.accept(this));
		block.accept(this);
		return null;
	}

	// static semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Type visitEmptyStmtSeq() {
		return null;
	}

	@Override
	public Type visitNonEmptyStmtSeq(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}
	// static semantics of expressions; a type is returned by the visitor

	@Override
	public AtomicType visitVector(Exp left, Exp right){
		checkBinOp(left, right, INT);
		return VECTOR;
	}
	@Override
	public AtomicType visitAdd(Exp left, Exp right) {
		var varLeft = left.accept(this);
		var varRight = right.accept(this);
		boolean wrongLeft = !(varLeft == VECTOR || varLeft == INT);
		boolean wrongRight = !(varRight == VECTOR || varRight == INT);
		if(varLeft == VECTOR && varRight == VECTOR)
			return VECTOR;

		else if (varLeft == INT && varRight == INT)
			return INT;

		else if(wrongRight && wrongLeft)
			throw new TypecheckerException(varLeft.toString() + " + " + varRight.toString(), "VECTOR + VECTOR or INT + INT");

		else if(wrongLeft)
			throw new TypecheckerException(varLeft.toString(), varRight.toString());

		throw new TypecheckerException(varRight.toString(), varLeft.toString());


	}

	@Override
	public AtomicType visitIntLiteral(int value) {
		return INT;
	}

	@Override
	public AtomicType visitMul(Exp left, Exp right) {
		var varLeft = left.accept(this);
		var varRight = right.accept(this);
		boolean wrongLeft = !(varLeft == VECTOR || varLeft == INT);
		boolean wrongRight = !(varRight == VECTOR || varRight == INT);
		if(varLeft == VECTOR && varRight == VECTOR)
			return INT;

		else if(varLeft == INT && varRight == INT)
			return INT;

		else if(varLeft == INT && varRight == VECTOR)
			return VECTOR;

		else if(varLeft == VECTOR && varRight == INT)
			return VECTOR;

		else if(wrongRight && wrongLeft)
			throw new TypecheckerException(varLeft.toString() + " + " + varRight.toString(), "VECTOR or INT * VECTOR or INT");

		else if(wrongLeft)
			throw new TypecheckerException(varLeft.toString(), "VECTOR or INT");

		else if(wrongRight)
			throw new TypecheckerException(varRight.toString(), "VECTOR or INT");

	throw new TypecheckerException(varRight.toString(), varLeft.toString());
	}

	@Override
	public AtomicType visitSign(Exp exp) {
		INT.checkEqual(exp.accept(this));
		return INT;
	}

	@Override
	public Type visitVariable(Variable var) {
		return env.lookup(var);
	}

	@Override
	public AtomicType visitNot(Exp exp) {
		BOOL.checkEqual(exp.accept(this));
		return BOOL;
	}

	@Override
	public AtomicType visitAnd(Exp left, Exp right) {
		checkBinOp(left, right, BOOL);
		return BOOL;
	}
	@Override
	public AtomicType visitBoolLiteral(boolean value) {
		return BOOL;
	}

	@Override
	public AtomicType visitEq(Exp left, Exp right) {
		left.accept(this).checkEqual(right.accept(this));
		return BOOL;
	}

	@Override
	public PairType visitPairLit(Exp left, Exp right) {
		return new PairType(left.accept(this), right.accept(this));
	}

	@Override
	public Type visitFst(Exp exp) {
		return exp.accept(this).getFstPairType();
	}

	@Override
	public Type visitSnd(Exp exp) {
		return exp.accept(this).getSndPairType();
	}

}