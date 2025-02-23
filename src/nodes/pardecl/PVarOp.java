package nodes.pardecl;

import nodes.expr.Identifier;
import abstractes.Node;
import interfaces.Visitor;

public class PVarOp extends Node {
    private Identifier id;
    private boolean ref = false;

    public PVarOp(Identifier id) {
        this.id = id;
    }

    public PVarOp(Identifier id, boolean ref) {
        this.id = id;
        this.ref = ref;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public boolean getRef() {
        return ref;
    }

    public void setRef(boolean ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "PVarOp{" +
                "id=" + id +
                ", ref='" + ref + '\'' +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
