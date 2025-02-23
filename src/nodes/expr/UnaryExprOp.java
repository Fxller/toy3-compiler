package nodes.expr;

import abstractes.Node;
import interfaces.Visitor;

public class UnaryExprOp extends Node implements ExprOp {
    private ExprOp expr;
    private String operator;

    public UnaryExprOp(ExprOp expr, String operator) {
        this.expr = expr;
        this.operator = operator;
    }

    public ExprOp getExpr() {
        return expr;
    }

    public void setExpr(ExprOp expr) {
        this.expr = expr;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCOperator(String op) {
        return switch (operator) {
            case "not" -> "!";
            default -> op;
        };
    }

    @Override
    public String toString() {
        return "UnaryExprOp{" +
                "expr=" + expr +
                ", operator='" + operator + '\'' +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
