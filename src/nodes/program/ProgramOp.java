package nodes.program;

import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class ProgramOp extends Node {
    private List<Object> varDeclOpsordefDeclOps;
    private BeginEndOp beginEndOp;

    public ProgramOp(List<Object> varDeclOpsordefDeclOps, BeginEndOp beginEndOp) {
        this.varDeclOpsordefDeclOps = varDeclOpsordefDeclOps;
        this.beginEndOp = beginEndOp;
    }

    public List<Object> getVarDeclOpsordefDeclOps() {
        return varDeclOpsordefDeclOps;
    }

    public void setVarDeclOpsordefDeclOps(List<Object> varDeclOpsordefDeclOps) {
        this.varDeclOpsordefDeclOps = varDeclOpsordefDeclOps;
    }

    public BeginEndOp getBeginEndOp() {
        return beginEndOp;
    }

    public void setBeginEndOp(BeginEndOp beginEndOp) {
        this.beginEndOp = beginEndOp;
    }

    @Override
    public String toString() {
        return "ProgramOp{" +
                "varDeclOpsordefDeclOps=" + varDeclOpsordefDeclOps +
                ", beginEndOp=" + beginEndOp +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
