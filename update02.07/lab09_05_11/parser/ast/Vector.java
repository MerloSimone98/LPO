package lab09_05_11.parser.ast;

import lab09_05_11.visitors.Visitor;
public class Vector implements Exp{
    final protected Exp right;
    final protected Exp left;
    public Vector(Exp left, Exp right){
        this.right = right;
        this.left = left;
    }

    public Exp leftVect(){
        return this.left;
    }

    public Exp rightVect(){
        return this.right;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "["+ left + ";" + right + "]" + ")";
    }
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVector(this.left, this.right);
    }
}
