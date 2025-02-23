package nodes.stat;

import nodes.expr.Identifier;
import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class ReadOp extends Node implements StatOp {
    private List<Identifier> ids;

    public ReadOp(List<Identifier> ids) {
        this.ids = ids;
    }

    public List<Identifier> getIds() {
        return ids;
    }

    public void setIds(List<Identifier> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "ReadOp{" +
                "ids=" + ids +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
