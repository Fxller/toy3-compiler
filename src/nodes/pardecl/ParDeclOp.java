package nodes.pardecl;

import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class ParDeclOp extends Node {
    private List<PVarOp> pVars;
    private String type;

    public ParDeclOp(List<PVarOp> pVars, String type) {
        this.pVars = pVars;
        this.type = type;
    }

    public List<PVarOp> getPVars() {
        return pVars;
    }

    public void setPVars(List<PVarOp> pVars) {
        this.pVars = pVars;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ParDeclOp{" +
                "pVars=" + pVars +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
