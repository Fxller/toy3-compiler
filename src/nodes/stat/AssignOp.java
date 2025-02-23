package nodes.stat;

import nodes.expr.ExprOp;
import nodes.expr.Identifier;
import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class AssignOp extends Node implements StatOp {
    private List<Identifier> ids;
    private List<ExprOp> exprOps;

    public AssignOp(List<Identifier> ids, List<ExprOp> exprOps) {
        this.ids = ids;
        this.exprOps = exprOps;
    }

    public List<Identifier> getIds() {
        return ids;
    }

    public void setIds(List<Identifier> ids) {
        this.ids = ids;
    }

    public List<ExprOp> getExpressions() {
        return exprOps;
    }

    public void setExpressions(List<ExprOp> exprOps) {
        this.exprOps = exprOps;
    }

    @Override
    public String toString() {
        return "AssignOp{" +
                "ids=" + ids +
                ", exprOps=" + exprOps +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
