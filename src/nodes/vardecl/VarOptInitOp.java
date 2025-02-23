package nodes.vardecl;

import nodes.expr.ExprOp;
import nodes.expr.Identifier;
import abstractes.Node;
import interfaces.Visitor;

public class VarOptInitOp extends Node {
    private Identifier id;
    private ExprOp exprOp;

    public VarOptInitOp(Identifier id, ExprOp exprOp) {
        this.id = id;
        this.exprOp = exprOp;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public ExprOp getExprOp() {
        return exprOp;
    }

    public void setExprOp(ExprOp exprOp) {
        this.exprOp = exprOp;
    }

    @Override
    public String toString() {
        return "VarOptInitOp{" +
                "id=" + id +
                ", exprOp=" + exprOp +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
