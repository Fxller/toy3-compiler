package nodes.program;

import nodes.stat.StatOp;
import nodes.vardecl.VarDeclOp;
import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class BeginEndOp extends Node {
    private List<VarDeclOp> varDeclOps;
    private List<StatOp> statOps;

    public BeginEndOp(List<VarDeclOp> varDeclOps, List<StatOp> statOps) {
        this.varDeclOps = varDeclOps;
        this.statOps = statOps;
    }

    public List<VarDeclOp> getVarDeclOps() {
        return varDeclOps;
    }

    public void setVarDeclOps(List<VarDeclOp> varDeclOps) {
        this.varDeclOps = varDeclOps;
    }

    public List<StatOp> getStatOps() {
        return statOps;
    }

    public void setStatOps(List<StatOp> statOps) {
        this.statOps = statOps;
    }

    @Override
    public String toString() {
        return "BeginEndOp{" +
                "varDeclOps=" + varDeclOps +
                ", statOps=" + statOps +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
