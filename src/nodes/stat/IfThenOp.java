package nodes.stat;

import nodes.body.BodyOp;
import nodes.expr.ExprOp;
import abstractes.Node;
import interfaces.Visitor;

public class IfThenOp extends Node implements StatOp {
    private ExprOp expr;
    private BodyOp body;

    public IfThenOp(ExprOp expr, BodyOp body) {
        this.expr = expr;
        this.body = body;
    }

    public ExprOp getExpr() {
        return expr;
    }

    public void setExpr(ExprOp expr) {
        this.expr = expr;
    }

    public BodyOp getBody() {
        return body;
    }

    public void setBody(BodyOp body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "IfThenOp{" +
                "expr=" + expr +
                ", body=" + body +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
