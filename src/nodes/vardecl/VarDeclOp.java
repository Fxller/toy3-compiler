package nodes.vardecl;

import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class VarDeclOp extends Node {
    private List<VarOptInitOp> varOptInitOps;
    private Object typeorconstant;

    public VarDeclOp(List<VarOptInitOp> varOptInitOps, Object typeorconstant) {
        this.varOptInitOps = varOptInitOps;
        this.typeorconstant = typeorconstant;
    }

    public List<VarOptInitOp> getVarOptInitOps() {
        return varOptInitOps;
    }

    public void setVarOptInitOps(List<VarOptInitOp> varOptInitOps) {
        this.varOptInitOps = varOptInitOps;
    }

    public Object getTypeorconstant() {
        return typeorconstant;
    }

    public void setTypeorconstant(Object typeorconstant) {
        this.typeorconstant = typeorconstant;
    }

    @Override
    public String toString() {
        return "VarDeclOp{" +
                "varOptInitOps=" + varOptInitOps +
                ", typeorconstant=" + typeorconstant +
                '}';
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
