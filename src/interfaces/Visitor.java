package interfaces;


import nodes.body.BodyOp;
import nodes.defdecl.DefDeclOp;
import nodes.expr.ExprOp;
import nodes.pardecl.PVarOp;
import nodes.pardecl.ParDeclOp;
import nodes.program.BeginEndOp;
import nodes.program.ProgramOp;
import nodes.stat.StatOp;
import nodes.vardecl.VarDeclOp;
import nodes.vardecl.VarOptInitOp;
import nodes.funcall.CallOp;

public interface Visitor {
    void visit(CallOp callOp);
    void visit(DefDeclOp defDeclOp);
    void visit(BodyOp bodyOp);
    void visit(ExprOp exprOp);
    void visit(ParDeclOp parDeclOp);
    void visit(PVarOp pVarOp);
    void visit(BeginEndOp beginEndOp);
    void visit(ProgramOp programOp);
    void visit(StatOp statOp);
    void visit(VarDeclOp varDeclOp);
    void visit(VarOptInitOp varOptInitOp);
}
