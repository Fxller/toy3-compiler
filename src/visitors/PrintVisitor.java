package visitors;

import nodes.body.BodyOp;
import nodes.defdecl.DefDeclOp;
import nodes.expr.*;
import nodes.pardecl.PVarOp;
import nodes.pardecl.ParDeclOp;
import nodes.program.BeginEndOp;
import nodes.program.ProgramOp;
import nodes.stat.*;
import nodes.funcall.*;
import nodes.vardecl.VarDeclOp;
import nodes.vardecl.VarOptInitOp;
import interfaces.Visitor;


import java.util.List;

public class PrintVisitor implements Visitor {
    private int indentation = 0;

    private void printIndentation() {
        for (int i = 0; i < indentation; i++) {
            // System.out.print("\t");
        }
    }

    @Override
    public void visit(ProgramOp programOp) {
        printIndentation();
        // System.out.println("ProgramOp");
        indentation++;
        List<Object> varDeclOpsorDefDeclOps = programOp.getVarDeclOpsordefDeclOps();
        if (varDeclOpsorDefDeclOps != null) {
            for (Object varDeclOpsorDefDeclOp : varDeclOpsorDefDeclOps) {
                if (varDeclOpsorDefDeclOp instanceof VarDeclOp) {
                    ((VarDeclOp) varDeclOpsorDefDeclOp).accept(this);
                } else if (varDeclOpsorDefDeclOp instanceof DefDeclOp) {
                    ((DefDeclOp) varDeclOpsorDefDeclOp).accept(this);
                }
            }
        }
        programOp.getBeginEndOp().accept(this);
        indentation--;
    }

    @Override
    public void visit(CallOp callOp) {
        printIndentation();
        // System.out.println("CallOp");
        indentation++;
        callOp.getId().accept(this);
        if (callOp.getArguments() != null) {
            for (ExprOp exprOp : callOp.getArguments()) {
                exprOp.accept(this);
            }
        }
        indentation--;
    }

    @Override
    public void visit(DefDeclOp defDeclOp) {
        printIndentation();
        // System.out.println("DefDeclOp");
        indentation++;
        defDeclOp.getId().accept(this);
        List<ParDeclOp> parDecls = defDeclOp.getParDecls();
        if (parDecls != null) {
            for (ParDeclOp parDeclOp : parDecls) {
                parDeclOp.accept(this);
            }
        }
        printIndentation();
        String type = defDeclOp.getType() == null ? "void" : defDeclOp.getType();
         // System.out.println("Type: " + type);
        defDeclOp.getBody().accept(this);
        indentation--;
    }

    @Override
    public void visit(BodyOp bodyOp) {
        printIndentation();
        // System.out.println("BodyOp");
        indentation++;
        if (bodyOp.getVarDecls() != null) {
            for (VarDeclOp varDeclOp : bodyOp.getVarDecls()) {
                varDeclOp.accept(this);
            }
        }
        if (bodyOp.getStats() != null) {
            for (StatOp statOp : bodyOp.getStats()) {
                statOp.accept(this);
            }
        }
        indentation--;
    }

    @Override
    public void visit(ExprOp exprOp) {
        printIndentation();
        indentation++;
        if (exprOp instanceof BinaryExprOp) {
            String operator = ((BinaryExprOp) exprOp).getOperator();
            switch (operator) {
                case "+": {
                    // System.out.println("AddOp");
                    break;
                }
                case "-": {
                    // System.out.println("SubOp");
                    break;
                }
                case "*": {
                    // System.out.println("MulOp");
                    break;
                }
                case "/": {
                    // System.out.println("DivOp");
                    break;
                }
                case ">": {
                    // System.out.println("GreatThenOp");
                    break;
                }
                case ">=": {
                    // System.out.println("GreatEqualOp");
                    break;
                }
                case "<": {
                    // System.out.println("LessThenOp");
                    break;
                }
                case "<=": {
                    // System.out.println("LessEqualOp");
                    break;
                }
                case "==": {
                    // System.out.println("EqualOp");
                    break;
                }
                case "<>": {
                    // System.out.println("NotEqualOp");
                    break;
                }
                case "and": {
                    // System.out.println("AndOp");
                    break;
                }
                case "or": {
                    // System.out.println("OrOp");
                    break;
                }
            }
            ((BinaryExprOp) exprOp).getLeft().accept(this);
            ((BinaryExprOp) exprOp).getRight().accept(this);
        }

        if (exprOp instanceof Identifier) {
            // System.out.println("identifier: " + ((Identifier) exprOp).getIdentifier());
        }

        if (exprOp instanceof UnaryExprOp) {
            String operator = ((UnaryExprOp) exprOp).getOperator();
            switch (operator) {
                case "-": {
                    // System.out.println("UnaryMinusOp");
                    break;
                }
                case "not": {
                    // System.out.println("NotOp");
                    break;
                }
            }
            ((UnaryExprOp) exprOp).getExpr().accept(this);
        }

        if (exprOp instanceof Constant) {
            // System.out.println("constant: " + ((Constant) exprOp).getValue());
        }

        indentation--;
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {
        printIndentation();
        String ref_temp = "";
        // System.out.println("ParDeclOp");
        indentation++;
        if (parDeclOp.getPVars() != null) {
            for (PVarOp pVarOp : parDeclOp.getPVars()) {
                if (pVarOp.getRef()) {
                    pVarOp.accept(this);
                    printIndentation();
                    // System.out.println("Type: ref " + parDeclOp.getType());
                } else {
                    pVarOp.accept(this);
                    printIndentation();
                    // System.out.println("Type: " + parDeclOp.getType());
                }
            }
        }
        indentation-=2;
    }

    @Override
    public void visit(PVarOp pVarOp) {
        printIndentation();
        // System.out.println("PVarOp");
        indentation++;
        pVarOp.getId().accept(this);
        indentation--;
    }

    @Override
    public void visit(BeginEndOp beginEndOp) {
        printIndentation();
        // System.out.println("BeginEndOp");
        indentation++;
        if (beginEndOp.getVarDeclOps() != null) {
            for (VarDeclOp varDeclOp : beginEndOp.getVarDeclOps()) {
                varDeclOp.accept(this);
            }
        }

        if (beginEndOp.getStatOps() != null) {
            for (StatOp statOp : beginEndOp.getStatOps()) {
                statOp.accept(this);
            }
        }
        indentation--;
    }

    @Override
    public void visit(StatOp statOp) {
        printIndentation();
        indentation++;
        // AssignOp
        if (statOp instanceof AssignOp) {
            // System.out.println("AssignOp");
            if (((AssignOp) statOp).getIds() != null) {
                for (Identifier id : ((AssignOp) statOp).getIds()) {
                    id.accept(this);
                }
            }
            if (((AssignOp) statOp).getExpressions() != null) {
                for (ExprOp exprOp : ((AssignOp) statOp).getExpressions()) {
                    exprOp.accept(this);
                }
            }
        }
        // IfThenElseOp
        if (statOp instanceof IfThenElseOp) {
            // System.out.println("IfThenElseOp");
            ((IfThenElseOp) statOp).getExpr().accept(this);
            ((IfThenElseOp) statOp).getThenBody().accept(this);
            ((IfThenElseOp) statOp).getElseBody().accept(this);
        }
        // IfThen
        if (statOp instanceof IfThenOp) {
            // System.out.println("IfThenOp");
            ((IfThenOp) statOp).getExpr().accept(this);
            ((IfThenOp) statOp).getBody().accept(this);
        }
        // WhileOp
        if (statOp instanceof WhileOp) {
            // System.out.println("WhileOp");
            ((WhileOp) statOp).getExpr().accept(this);
            ((WhileOp) statOp).getBody().accept(this);
        }
        // WriteOp
        if (statOp instanceof WriteOp) {
            // System.out.println("WriteOp");
            if (((WriteOp) statOp).getExpressions() != null) {
                for (ExprOp exprOp : ((WriteOp) statOp).getExpressions()) {
                    exprOp.accept(this);
                }
            }
            if (((WriteOp) statOp).getNewLine()) {
                printIndentation();
                // System.out.println("NewLine: \n");
            }
        }
        // ReadOp
        if (statOp instanceof ReadOp) {
            // System.out.println("ReadOp");
            if (((ReadOp) statOp).getIds() != null) {
                for (Identifier id : ((ReadOp) statOp).getIds()) {
                    id.accept(this);
                }
            }
        }
        // ReturnOp
        if (statOp instanceof ReturnOp) {
            // System.out.println("ReturnOp");
            if (((ReturnOp) statOp).getExpr() != null) {
                ((ReturnOp) statOp).getExpr().accept(this);
            }
        }
        indentation--;
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {
        printIndentation();
        // System.out.println("VarDeclOp");
        indentation++;
        if (varDeclOp.getVarOptInitOps() != null) {
            for (VarOptInitOp varOptInitOp : varDeclOp.getVarOptInitOps()) {
                varOptInitOp.accept(this);
            }
        }
        printIndentation();
        if (varDeclOp.getTypeorconstant() instanceof String) {
            // System.out.println("Type: " + varDeclOp.getTypeorconstant());
        } else if (varDeclOp.getTypeorconstant() instanceof Constant) {
            indentation = indentation - 2;
            ((ExprOp) varDeclOp.getTypeorconstant()).accept(this);
            indentation = indentation + 2;
            printIndentation();
            // System.out.println("Type: " + ((Constant) varDeclOp.getTypeorconstant()).getConstantType());
        }
        indentation--;
    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {
        printIndentation();
        // System.out.println("VarOptInitOp");
        indentation++;
        varOptInitOp.getId().accept(this);
        if (varOptInitOp.getExprOp() != null) {
            varOptInitOp.getExprOp().accept(this);
        }
        indentation--;

    }
}
