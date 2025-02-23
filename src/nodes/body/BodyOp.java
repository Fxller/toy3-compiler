package nodes.body;

import nodes.stat.StatOp;
import nodes.vardecl.VarDeclOp;
import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class BodyOp extends Node{
    private List<VarDeclOp> varDecls;
    private List<StatOp> stats;

    public BodyOp(List<VarDeclOp> varDecls, List<StatOp> stats) {
        this.varDecls = varDecls;
        this.stats = stats;
    }

    public List<VarDeclOp> getVarDecls() {
        return varDecls;
    }

    public void setVarDecls(List<VarDeclOp> varDecls) {
        this.varDecls = varDecls;
    }

    public List<StatOp> getStats() {
        return stats;
    }

    public void setStats(List<StatOp> stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "BodyOp{" +
                "varDecls=" + varDecls +
                ", stats=" + stats +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
