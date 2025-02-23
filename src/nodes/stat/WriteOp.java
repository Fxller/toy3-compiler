package nodes.stat;

import nodes.expr.ExprOp;
import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class WriteOp extends Node implements StatOp {
    private List<ExprOp> exprOps;
    private boolean newLine;

    public WriteOp(List<ExprOp> exprOps, boolean newLine) {
        this.exprOps = exprOps;
        this.newLine = newLine;
    }

    public List<ExprOp> getExpressions() {
        return exprOps;
    }

    public void setExpressions(List<ExprOp> exprOps) {
        this.exprOps = exprOps;
    }

    public boolean getNewLine() {
        return newLine;
    }

    public void setNewLine(boolean newLine) {
        this.newLine = newLine;
    }

    @Override
    public String toString() {
        return "WriteOp{" +
                "exprOps=" + exprOps +
                ", newLine=" + newLine +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
