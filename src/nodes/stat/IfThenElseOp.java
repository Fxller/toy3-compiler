package nodes.stat;

import nodes.body.BodyOp;
import nodes.expr.ExprOp;
import abstractes.Node;
import interfaces.Visitor;

public class IfThenElseOp extends Node implements StatOp {
    private ExprOp expr;
    private BodyOp thenBody, elseBody;


    public IfThenElseOp(ExprOp expr, BodyOp thenBody, BodyOp elseBody) {
        this.expr = expr;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    public ExprOp getExpr() {
        return expr;
    }

    public void setExpr(ExprOp expr) {
        this.expr = expr;
    }

    public BodyOp getThenBody() {
        return thenBody;
    }

    public void setThenBody(BodyOp thenBody) {
        this.thenBody = thenBody;
    }

    public BodyOp getElseBody() {
        return elseBody;
    }

    public void setElseBody(BodyOp elseBody) {
        this.elseBody = elseBody;
    }

    @Override
    public String toString() {
        return "IfThenElseOp{" +
                "expr=" + expr +
                ", thenBody=" + thenBody +
                ", elseBody=" + elseBody +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
