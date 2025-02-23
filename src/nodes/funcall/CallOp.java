package nodes.funcall;

import nodes.expr.ExprOp;
import nodes.expr.Identifier;
import nodes.stat.StatOp;
import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class CallOp extends Node implements StatOp, ExprOp {
    private Identifier id;
    private List<ExprOp> arguments;

    public CallOp(Identifier id, List<ExprOp> arguments) {
        this.id = id;
        this.arguments = arguments;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public List<ExprOp> getArguments() {
        return arguments;
    }

    public void setArguments(List<ExprOp> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "CallOp{" +
                "id=" + id +
                ", arguments=" + arguments +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}