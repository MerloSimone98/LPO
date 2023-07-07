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
		String vString = "";
		if (exp.accept(this) instanceof VectorValue v){
			vString += "[";
		for (int i = 0; i < v.value.length; i++) {
			vString += v.value[i].toString();
			if(i < v.value.length - 1)
				vString += ";";
		}
		vString += "]";
		printWriter.println(vString);
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

	@Override
	public Value visitForeach(Variable var, Exp exp, Block block){
		if(!(exp.accept(this) instanceof VectorValue v))
			throw new InterpreterException("Expression have to result in a Vector");
		env.enterScope();
		var aux = visitVariable(var);
		IntLiteral toReturn = new IntLiteral (aux.toInt());
		for(int i = 0; i < v.value.length; i++){
			visitAssignStmt(var, new IntLiteral(v.value[i]));
			block.accept(this);
		}
		env.exitScope();
		visitAssignStmt(var, toReturn);
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
		int pos = left.accept(this).toInt();
		int dim = right.accept(this).toInt();
		if(dim < 0)
			throw  new InterpreterException ("java.lang.NegativeArraySizeException: " + dim + "\n");
		else if(pos < 0 || pos > dim - 1)
			throw new InterpreterException ("java.lang.ArrayIndexOutOfBoundsException: Index " + pos + " out of bounds for length " + dim + "\n");

		Integer[] arr = new Integer[dim];
		for(int i = 0; i < arr.length; i++)
			arr[i] = 0;
		arr[pos] = 1;
		return new VectorValue(arr);
	}

	@Override
	public Value visitAdd(Exp left, Exp right) {
		if(left.accept(this) instanceof VectorValue v1 && right.accept(this) instanceof VectorValue v2){
			if(v1.value.length != v2.value.length)
				throw new InterpreterException("Vectors must have the same dimension");
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
		if(left.accept(this) instanceof VectorValue v1 && right.accept(this) instanceof VectorValue v2){
			if(v1.value.length != v2.value.length)
				throw new InterpreterException("Vectors must have the same dimension");
			IntValue res = new IntValue(0);
		for(int i = 0; i < v1.value.length; i++)
			res.value += v1.value[i] * v2.value[i];
		return res;
		}
		else if(left.accept(this) instanceof VectorValue v1 && right.accept(this) instanceof IntValue){
			int n = right.accept(this).toInt();
			for(int i = 0; i < v1.value.length; i++)
				v1.value[i] *= n;
			return v1;
		}
		else if(left.accept(this) instanceof IntValue && right.accept(this) instanceof VectorValue v1){
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
		if(left.accept(this) instanceof VectorValue v1 && right.accept(this) instanceof VectorValue v2){
			if(v1.value.length != v2.value.length)
				return new BoolValue(false);

			for(int i = 0; i < v1.value.length; i++){
				if(v1.value[i] != v2.value[i])
					return new BoolValue(false);
			}
		return new BoolValue(true);
		}
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
