package nodes.stat;

import nodes.expr.ExprOp;
import abstractes.Node;
import interfaces.Visitor;

public class ReturnOp extends Node implements StatOp {
    private ExprOp expr;

    public ReturnOp(ExprOp expr) {
        this.expr = expr;
    }

    public ExprOp getExpr() {
        return expr;
    }

    public void setExpr(ExprOp expr) {
        this.expr = expr;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
