package nodes.expr;

import abstractes.Node;
import interfaces.Visitor;

public class Constant extends Node implements ExprOp {
    private Object value;
    private final String[] typeConstant = {"int", "double", "char", "string", "bool"};

    public Constant(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getConstantType() {
        if (this.getValue() instanceof Integer) {
            return typeConstant[0];
        } else if (this.getValue() instanceof Double) {
            return typeConstant[1];
        } else if (this.getValue() instanceof Character) {
            return typeConstant[2];
        } else if (this.getValue() instanceof String) {
            return typeConstant[3];
        } else if (this.getValue() instanceof Boolean) {
            return typeConstant[4];
        }
        return null;
    }

    @Override
    public String toString() {
        return "Constant{" +
                "value=" + value +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
