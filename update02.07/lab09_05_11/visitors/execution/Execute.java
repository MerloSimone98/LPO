package lab09_05_11.visitors.execution;

import java.io.PrintWriter;

import lab09_05_11.environments.EnvironmentException;
import lab09_05_11.environments.GenEnvironment;
import lab09_05_11.parser.ast.*;
import lab09_05_11.visitors.Visitor;

import static java.util.Objects.requireNonNull;

public class Execute implements Visitor<Value> {

	private final GenEnvironment<Value> env = new GenEnvironment<>();
	private final PrintWriter printWriter; // output stream used to print values

	public Execute() {
		printWriter = new PrintWriter(System.out, true);
	}

	public Execute(PrintWriter printWriter) {
		this.printWriter = requireNonNull(printWriter);
	}

	// dynamic semantics for programs; no value returned by the visitor

	@Override
	public Value visitMyLangProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
			// possible runtime errors
			// EnvironmentException: undefined variable
		} catch (EnvironmentException e) {
			throw new InterpreterException(e);
		}
		return null;
	}

	// dynamic semantics for statements; no value returned by the visitor

	@Override
	public Value visitAssignStmt(Variable var, Exp exp) {
		env.update(var, exp.accept(this));
		return null;
	}
	@Override
	public Value visitPrintStmt(Exp exp) {
		if (exp.accept(this) instanceof VectorValue){
			System.out.print("[");
		for (int i = 0; i < ((VectorValue) exp.accept(this)).value.length; i++) {
			System.out.print(((VectorValue) exp.accept(this)).value[i]);
			if(i < ((VectorValue) exp.accept(this)).value.length - 1)
				System.out.print(";");
		}
		System.out.print("]\n");
		}
		else
			printWriter.println(exp.accept(this));
		return null;
	}

	@Override
	public Value visitVarStmt(Variable var, Exp exp) {
		env.dec(var, exp.accept(this));
		return null;
	}

	@Override
	public Value visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
		if (exp.accept(this).toBool())
			thenBlock.accept(this);
		else if (elseBlock != null)
			elseBlock.accept(this);
		return null;
	}

	@Override
	public Value visitBlock(StmtSeq stmtSeq) {
		env.enterScope();
		stmtSeq.accept(this);
		env.exitScope();
		return null;
	}

	// dynamic semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Value visitEmptyStmtSeq() {
		return null;
	}

	@Override
	public Value visitNonEmptyStmtSeq(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// dynamic semantics of expressions; a value is returned by the visitor

	@Override
	public VectorValue visitVector(Exp left, Exp right){
		Integer[] arr = new Integer[right.accept(this).toInt()];
		for(int i = 0; i < arr.length; i++){
			if(left.accept(this).toInt() == i)
				arr[i] = 1;
			else
				arr[i] = 0;
		}
		return new VectorValue(arr);
	}

	@Override
	public Value visitAdd(Exp left, Exp right) {
		if(left.accept(this) instanceof VectorValue && right.accept(this) instanceof VectorValue){
			VectorValue v1 = visitVector(((Vector) left).leftVect(), ((Vector) left).rightVect());
			VectorValue v2 = visitVector(((Vector) right).leftVect(), ((Vector) right).rightVect());
			for(int i = 0; i < v1.value.length; i++)
				v1.value[i] += v2.value[i];
			return v1;
		}
		return new IntValue(left.accept(this).toInt() + right.accept(this).toInt());
	}

	@Override
	public IntValue visitIntLiteral(int value) {
		return new IntValue(value);
	}

	@Override
	public Value visitMul(Exp left, Exp right) {
		if(left.accept(this) instanceof VectorValue && right.accept(this) instanceof VectorValue){
			VectorValue v1 = visitVector(((Vector) left).leftVect(), ((Vector) left).rightVect());
			VectorValue v2 = visitVector(((Vector) right).leftVect(), ((Vector) right).rightVect());
		for(int i = 0; i < v1.value.length; i++)
			v1.value[i] *= v2.value[i];
		return v1;
		}
		else if(left.accept(this) instanceof VectorValue && right.accept(this) instanceof IntValue){
			VectorValue v1 = visitVector(((Vector) left).leftVect(), ((Vector) left).rightVect());
			int n = right.accept(this).toInt();
			for(int i = 0; i < v1.value.length; i++)
				v1.value[i] *= n;
			return v1;
		}
		else if(left.accept(this) instanceof IntValue && right.accept(this) instanceof VectorValue){
			VectorValue v1 = visitVector(((Vector) right).leftVect(), ((Vector) right).rightVect());
			int n = left.accept(this).toInt();
			for(int i = 0; i < v1.value.length; i++)
				v1.value[i] *= n;
			return v1;
		}
		return new IntValue(left.accept(this).toInt() * right.accept(this).toInt());
	}

	@Override
	public IntValue visitSign(Exp exp) {
		return new IntValue(-exp.accept(this).toInt());
	}

	@Override
	public Value visitVariable(Variable var) {
		return env.lookup(var);
	}

	@Override
	public BoolValue visitNot(Exp exp) {
		return new BoolValue(!exp.accept(this).toBool());
	}

	@Override
	public BoolValue visitAnd(Exp left, Exp right) {
		return new BoolValue(left.accept(this).toBool() && right.accept(this).toBool());
	}

	@Override
	public BoolValue visitBoolLiteral(boolean value) {
		return new BoolValue(value);
	}

	@Override
	public BoolValue visitEq(Exp left, Exp right) {
		return new BoolValue(left.accept(this).equals(right.accept(this)));
	}

	@Override
	public PairValue visitPairLit(Exp left, Exp right) {
		return new PairValue(left.accept(this), right.accept(this));
	}

	@Override
	public Value visitFst(Exp exp) {
		return exp.accept(this).toPair().getFstVal();
	}

	@Override
	public Value visitSnd(Exp exp) {
		return exp.accept(this).toPair().getSndVal();
	}

}
