package visitors;

import nodes.body.BodyOp;
import nodes.defdecl.DefDeclOp;
import nodes.expr.*;
import nodes.funcall.CallOp;
import nodes.pardecl.PVarOp;
import nodes.pardecl.ParDeclOp;
import nodes.program.BeginEndOp;
import nodes.program.ProgramOp;
import nodes.stat.*;
import nodes.vardecl.VarDeclOp;
import nodes.vardecl.VarOptInitOp;
import interfaces.Visitor;
import enviroment.Scope;
import enviroment.SymbolTable;

import java.util.Objects;
import java.util.StringJoiner;

public class Scoping implements Visitor {
    private final SymbolTable table = new SymbolTable();
    private String tempType;

    @Override
    public void visit(CallOp callOp) {
    }

    @Override
    public void visit(DefDeclOp defDeclOp) {
        // aggiunge la firma della funzione allo scope globals
        if (table.probe(Scope.Group.FUN, defDeclOp.getId().getIdentifier())) {
            System.err.print("Function " + defDeclOp.getId().getIdentifier() + " already declared with type " + table.lookup(Scope.Group.FUN, defDeclOp.getId().getIdentifier()));
            System.exit(1);
        }

        String funType = functionParameters(defDeclOp);
        table.addId(Scope.Group.FUN, defDeclOp.getId().getIdentifier(), funType);
        defDeclOp.getId().setType(funType);

        // viene creato lo scope per il corpo della funzione
        table.enterScope();
        defDeclOp.setScope(table.getCurrentScope());
        if (defDeclOp.getParDecls() != null)
            for (ParDeclOp parDeclOp : defDeclOp.getParDecls())
                parDeclOp.accept(this);
        if (defDeclOp.getBody().getVarDecls() != null)
            for (VarDeclOp varDeclOp : defDeclOp.getBody().getVarDecls())
                varDeclOp.accept(this);
        if (defDeclOp.getBody().getStats() != null)
            for (StatOp statOp : defDeclOp.getBody().getStats())
                statOp.accept(this);
        // System.out.println("Scope in DefDeclOp: ");
        // table.printTable();
        table.exitScope();
    }

    @Override
    public void visit(BodyOp bodyOp) {
        table.enterScope();
        bodyOp.setScope(table.getCurrentScope());
        if (bodyOp.getVarDecls() != null)
            for (VarDeclOp varDeclOp : bodyOp.getVarDecls())
                varDeclOp.accept(this);
        if (bodyOp.getStats() != null)
            for (StatOp statOp : bodyOp.getStats())
                statOp.accept(this);
        bodyOp.setType("notype");
        // System.out.println("Scope in BodyOp: ");
        // table.printTable();
        table.exitScope();
    }

    @Override
    public void visit(ExprOp exprOp) {
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {
        tempType = parDeclOp.getType();
        if (parDeclOp.getPVars() != null)
            for (PVarOp pVar : parDeclOp.getPVars())
                pVar.accept(this);
    }

    @Override
    public void visit(PVarOp pVarOp) {
        if (table.probe(Scope.Group.VAR, pVarOp.getId().getIdentifier())) {
            if (Objects.equals(table.lookup(Scope.Group.VAR, pVarOp.getId().getIdentifier()), tempType)) {
                System.err.print("Redefinition of parameter " + pVarOp.getId());
                System.exit(1);
            }
            System.err.print("Conflicting types for " + pVarOp.getId() + "; Previous definition have type " + table.lookup(Scope.Group.VAR, pVarOp.getId().getIdentifier()));
            System.exit(1);
        }
        if (pVarOp.getRef()) {
            table.addId(Scope.Group.VAR, pVarOp.getId().getIdentifier(), "ref " + tempType);
            pVarOp.getId().setType("ref " + tempType);
        } else {
            table.addId(Scope.Group.VAR, pVarOp.getId().getIdentifier(), tempType);
            pVarOp.getId().setType(tempType);
        }
    }

    @Override
    public void visit(BeginEndOp beginEndOp) {
        table.enterScope();
        beginEndOp.setScope(table.getCurrentScope());
        if (beginEndOp.getVarDeclOps() != null)
            for (VarDeclOp varDeclOp : beginEndOp.getVarDeclOps())
                varDeclOp.accept(this);
        if (beginEndOp.getStatOps() != null)
            for (StatOp statOp : beginEndOp.getStatOps())
                statOp.accept(this);
        // System.out.println("Scope in BeginEndOp: ");
        // table.printTable();
        table.exitScope();
    }

    @Override
    public void visit(ProgramOp programOp) {
        table.enterScope();
        programOp.setScope(table.getCurrentScope());
        if (programOp.getVarDeclOpsordefDeclOps() != null)
            for (Object obj : programOp.getVarDeclOpsordefDeclOps())
                if (obj instanceof VarDeclOp varDeclOp)
                    varDeclOp.accept(this);
                else if (obj instanceof DefDeclOp defDeclOp)
                    defDeclOp.accept(this);
        programOp.getBeginEndOp().accept(this);
        // System.out.println("Scope in ProgramOp: ");
        // table.printTable();
        table.exitScope();
    }

    @Override
    public void visit(StatOp statOp) {
        if (statOp instanceof IfThenElseOp ifThenElseOp) {
            ifThenElseOp.getThenBody().accept(this);
            ifThenElseOp.getElseBody().accept(this);
        } else if (statOp instanceof IfThenOp ifThenOp) {
            ifThenOp.getBody().accept(this);
        } else if (statOp instanceof WhileOp whileOp) {
            whileOp.getBody().accept(this);
        }
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {
        if (varDeclOp.getTypeorconstant() instanceof Constant) {
            // Inferenza di tipo solo su una singola variabile
            if (varDeclOp.getVarOptInitOps().size() > 1) {
                System.err.print("Only one variable can be declared at a time with const");
                System.exit(1);
            }
            // Controlla che se una variabile è inizializzata non venga effettuat inferenza di tipo
            varDeclOp.getVarOptInitOps().forEach(varOptInitOp -> {
                if (varOptInitOp.getExprOp() != null) {
                    System.err.print("A variable declared with 'const' must not be initialized.");
                    System.exit(1);
                }
            });
        }

        if (varDeclOp.getVarOptInitOps() != null)
            varDeclOp.getVarOptInitOps().forEach(varOptInitOp -> {
                if (varDeclOp.getTypeorconstant() instanceof String type)
                    tempType = type;
                else if (varDeclOp.getTypeorconstant() instanceof Constant constant) {
                    tempType = constant.getConstantType();
                    varDeclOp.getVarOptInitOps().get(0).setExprOp(constant);
                }
                varOptInitOp.accept(this);
            });
    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {
        if (table.probe(Scope.Group.VAR, varOptInitOp.getId().getIdentifier())) {
            if (Objects.equals(table.lookup(Scope.Group.VAR, varOptInitOp.getId().getIdentifier()), tempType)) {
                System.err.print("Redeclaration of " + varOptInitOp.getId().getIdentifier());
                System.exit(1);
            }
            System.err.print("Conflicting types for " + varOptInitOp.getId().getIdentifier() + "; Previous declaration have type " + table.lookup(Scope.Group.VAR, varOptInitOp.getId().getIdentifier()));
            System.exit(1);
        }
        table.addId(Scope.Group.VAR, varOptInitOp.getId().getIdentifier(), tempType);
        varOptInitOp.getId().setType(tempType);
    }

    private String functionParameters(DefDeclOp defDeclOp) {
        StringBuilder sb = new StringBuilder();

        if (defDeclOp.getType() != null)
            sb.append(defDeclOp.getType());
        else
            sb.append("void");
        sb.append("(");

        if (defDeclOp.getParDecls() != null) {
            StringJoiner joiner = new StringJoiner(", ");
            for (ParDeclOp parDeclOp : defDeclOp.getParDecls()) {
                for (PVarOp pVarOp : parDeclOp.getPVars()) {
                    String param = (pVarOp.getRef() ? "ref " : "") + parDeclOp.getType();
                    joiner.add(param);
                }
            }
            sb.append(joiner);
        }

        sb.append(")");
        return sb.toString();
    }
}
