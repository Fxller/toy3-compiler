package nodes.expr;

import abstractes.Node;
import interfaces.Visitor;

public class BinaryExprOp  extends Node implements ExprOp {
    private ExprOp left, right;
    private String operator;
    
    public BinaryExprOp(ExprOp left, ExprOp right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public ExprOp getLeft() {
        return left;
    }

    public void setLeft(ExprOp left) {
        this.left = left;
    }

    public ExprOp getRight() {
        return right;
    }

    public void setRight(ExprOp right) {
        this.right = right;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    public String getOperator() {
        return operator;
    }

    public String getCOperator(String op) {
        return switch (operator) {
            case "and" -> "&&";
            case "or" -> "||";
            case "<>" -> "!=";
            default -> op;
        };
    }

    @Override
    public String toString() {
        return "BinaryExprOp{" +
                "left=" + left +
                ", right=" + right +
                ", operator='" + operator + '\'' +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
