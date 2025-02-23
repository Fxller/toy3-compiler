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
import utils.BinaryOpTable;
import utils.UnaryOpTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeChecking implements Visitor {
    private final SymbolTable table = new SymbolTable();
    private String funType;
    private boolean isFunction = false;

    @Override
    public void visit(CallOp callOp) {
        isFunction = true;
        // Setta il tipo dell'identificatore della funzione
        callOp.getId().accept(this);
        // Prendi il tipo della funzione
        String typefun = callOp.getId().getType();

        List<String> typefunList;
        //In caso i parametri siano vuoti setto l'arraylist a null
        if (typefun.substring(typefun.indexOf("(") + 1, typefun.indexOf(")")).isEmpty()) {
            typefunList = null;
        } else {
            typefun = extractParameters(typefun);
            typefunList = new ArrayList<>(List.of(typefun.split(", ")));

        }

        // se i paramtri sono uguale a 0 ma gli argomenti no devi dare errore
        if (typefunList == null && callOp.getArguments() != null) {
            System.err.print("Function " + callOp.getId().getIdentifier() + " called with " + callOp.getArguments().size() + " parameters, but it does not require parameters");
            System.exit(1);
            // Se la lista di paramtri della funzione non è uguale a null ma gli argomenti si si deve dare errore
        } else if (typefunList != null && callOp.getArguments() == null) {
            System.err.print("Function " + callOp.getId().getIdentifier() + " called with 0 parameters, but it requires " + typefunList.size());
            System.exit(1);
        }

        if (callOp.getArguments() != null) {
            if (typefunList.size() != callOp.getArguments().size()) {
                System.err.print("Function " + callOp.getId().getIdentifier() + " called with " + callOp.getArguments().size() + " parameters, but it requires " + typefunList.size());
                System.exit(1);
            }
            // Controlla se i tipi dei parametri passati alla funzione sono compatibili con quelli richiesti
            for (int i = 0; i < typefunList.size(); i++) {
                String parameterType = typefunList.get(i).replace("ref ", "");
                callOp.getArguments().get(i).accept(this);
                String exprType = callOp.getArguments().get(i).getType().replace("ref ", "");
                if (!Objects.equals(parameterType, exprType)) {
                    System.err.print("Function " + callOp.getId().getIdentifier() + " called with parameter at position " + (i + 1) + " of type " + exprType + " but it requires " + parameterType);
                    System.exit(1);
                }
            }
            // Se il parametro è passato per riferimento, controlla che l'argomento sia una variabile
            for (int i = 0; i < typefunList.size(); i++) {
                if (typefunList.get(i).startsWith("ref") && !(callOp.getArguments().get(i) instanceof Identifier)) {
                    System.err.print("Function " + callOp.getId().getIdentifier() + " called with non-variable argument at position " + (i + 1));
                    System.exit(1);
                }
            }
        }
        typefun = extractType(callOp.getId().getType());
        if (!typefun.equals("void"))
            callOp.setType(typefun);
    }

    @Override
    public void visit(DefDeclOp defDeclOp) {
        table.setCurrentScope(defDeclOp.getScope());
        String type = defDeclOp.getId().getType();
        funType = extractType(type);
        defDeclOp.setType(funType);

        // controlla che la funzione abbia almeno un return statement nel caso in cui il suo tipo sia diverso da void
        if (!defDeclOp.getType().equals("void")) {
            boolean hasReturnOp = defDeclOp.getBody().getStats() != null &&
                    defDeclOp.getBody().getStats().stream().anyMatch(statOp -> statOp instanceof ReturnOp);

        // Nel caso in cui il corpo della funzione sia presente un if-then-else statement, controlla che entrambi i rami abbiano un return statement
            // nel caso in cui non ci sia un return statement nel corpo della funzione
            if (!hasReturnOp && defDeclOp.getBody().getStats() != null) {
                List<StatOp> stats = defDeclOp.getBody().getStats();
                for (StatOp statOp : stats) {
                    if (statOp instanceof IfThenElseOp) {
                        if (!((IfThenElseOp)statOp).getThenBody().getStats().stream().anyMatch(s -> s instanceof ReturnOp)) {
                            System.err.print("Missing return in the then branch of the if-then-else statement for function " + defDeclOp.getId().getIdentifier());
                            System.exit(1);
                        } else if (!((IfThenElseOp)statOp).getElseBody().getStats().stream().anyMatch(s -> s instanceof ReturnOp)) {
                            System.err.print("Missing return in the else branch of the if-then-else statement for function " + defDeclOp.getId().getIdentifier());
                            System.exit(1);
                        } else {
                            hasReturnOp = true;
                            break;
                        }
                    }
                }
            }
            if (!hasReturnOp) {
                System.err.print("Function " + defDeclOp.getId().getIdentifier() + " must have at least one return statement");
                System.exit(1);
            }
        }

        if (defDeclOp.getParDecls() != null)
            defDeclOp.getParDecls().forEach(parDeclOp -> parDeclOp.accept(this));
        if (defDeclOp.getBody().getVarDecls() != null)
            defDeclOp.getBody().getVarDecls().forEach(varDeclOp -> varDeclOp.accept(this));
        if (defDeclOp.getBody().getStats() != null)
            defDeclOp.getBody().getStats().forEach(statOp -> statOp.accept(this));
    }

    @Override
    public void visit(BodyOp bodyOp) {
        table.setCurrentScope(bodyOp.getScope());
        bodyOp.getVarDecls().forEach(e -> e.accept(this));
        bodyOp.getStats().forEach(e -> e.accept(this));
    }

    @Override
    public void visit(ExprOp exprOp) {
        // Se il nodo e' legato ad una costante, node.type = constant.type
        if (exprOp instanceof Constant constant) {
            constant.setType(constant.getConstantType());
        }// Se il nodo è legato ad un uso di un identificatore, node.type = lookup(identificatore)
        else if (exprOp instanceof Identifier id) {
            String type;
            if (isFunction) {
                type = table.lookup(Scope.Group.FUN, id.getIdentifier());
                if (type == null) {
                    System.err.print("Function " + id.getIdentifier() + " not declared");
                    System.exit(1);
                }
                isFunction = false;
            } else {
                type = table.lookup(Scope.Group.VAR, id.getIdentifier());
                if (type == null) {
                    System.err.print("Variable " + id.getIdentifier() + " not declared");
                    System.exit(1);
                }
            }
            id.setType(type);
        } else if (exprOp instanceof UnaryExprOp unaryExprOp) {
            // Se il nodo è legato ad un operatore unario, node.type = unaryTable.getResult(operator, operand)
            unaryExprOp.getExpr().accept(this);
            String operandType = unaryExprOp.getExpr().getType();
            String operator = unaryExprOp.getOperator();
            String result = UnaryOpTable.getResult(operator, operandType);
            if (result == null) {
                System.err.print("Invalid unary operation " + operator + " on " + operandType);
                System.exit(1);
            } else
                unaryExprOp.setType(result);
        } else if (exprOp instanceof BinaryExprOp binaryExprOp) {
            // Se il nodo è legato ad un operatore binario, node.type = binaryTable.getResult(operator, operand1, operand2)
            binaryExprOp.getLeft().accept(this);
            binaryExprOp.getRight().accept(this);
            String operand1Type = binaryExprOp.getLeft().getType().replace("ref ", "");
            String operand2Type = binaryExprOp.getRight().getType().replace("ref ", "");

            String operator = binaryExprOp.getOperator();
            String result = BinaryOpTable.getResult(operator, operand1Type, operand2Type);
            if (result == null) {
                System.err.print("Invalid binary operation " + operator + " on " + operand1Type + " and " + operand2Type);
                System.exit(1);
            } else
                binaryExprOp.setType(result);
        }
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {
        parDeclOp.getPVars().forEach(pVarOp -> pVarOp.accept(this));

    }

    @Override
    public void visit(PVarOp pVarOp) {
    }

    @Override
    public void visit(BeginEndOp beginEndOp) {
        table.setCurrentScope(beginEndOp.getScope());
        if (beginEndOp.getVarDeclOps() != null)
            beginEndOp.getVarDeclOps().forEach(varDeclOp -> varDeclOp.accept(this));
        if (beginEndOp.getStatOps() != null)
            beginEndOp.getStatOps().forEach(statOp -> statOp.accept(this));
    }

    @Override
    public void visit(ProgramOp programOp) {
        table.setCurrentScope(programOp.getScope());
        programOp.getVarDeclOpsordefDeclOps().forEach(e -> {
            if (e instanceof DefDeclOp defDeclOp)
                defDeclOp.accept(this);
            else if (e instanceof VarDeclOp varDeclOp)
                varDeclOp.accept(this);
        });
        programOp.getBeginEndOp().accept(this);
    }

    @Override
    public void visit(StatOp statOp) {
        if (statOp instanceof IfThenElseOp ifThenElseOp) {
            ifThenElseOp.getExpr().accept(this);
            ifThenElseOp.getThenBody().accept(this);
            ifThenElseOp.getElseBody().accept(this);
            if (ifThenElseOp.getExpr().getType().equals("bool") && ifThenElseOp.getThenBody().getType().equals("notype") && ifThenElseOp.getElseBody().getType().equals("notype"))
                ifThenElseOp.setType("notype");
            else {
                System.err.print("Invalid types in if statement");
                System.exit(0);
            }
        } else if (statOp instanceof IfThenOp ifThenOp) {
            ifThenOp.getExpr().accept(this);
            ifThenOp.getBody().accept(this);
            if (ifThenOp.getExpr().getType().equals("bool") && ifThenOp.getBody().getType().equals("notype"))
                ifThenOp.setType("notype");
            else {
                System.err.print("Invalid types in if statement");
                System.exit(1);
            }
        } else if (statOp instanceof WhileOp whileOp) {
            whileOp.getExpr().accept(this);
            whileOp.getBody().accept(this);
            if (whileOp.getExpr().getType().equals("bool") && whileOp.getBody().getType().equals("notype"))
                whileOp.setType("notype");
            else {
                System.err.print("Invalid types in while statement");
                System.exit(1);
            }
        } else if (statOp instanceof AssignOp assignOp) {
            // Se l'assegnamento è un multiple assign, controlla che non ci siano chiamate a funzioni
            if (assignOp.getExpressions().size() > 1)
                assignOp.getExpressions().forEach(expr -> {
                    if (expr instanceof CallOp) {
                        System.err.print("Cannot assign a function call to a variable in a multiple assign statement");
                        System.exit(1);
                    }
                });
            if (assignOp.getIds().size() != assignOp.getExpressions().size()) {
                System.err.print("Number of identifiers and expressions in assignment do not match");
                System.exit(1);
            }
            assignOp.getIds().forEach(id -> id.accept(this));
            assignOp.getExpressions().forEach(expr -> expr.accept(this));

            // Controlla se tutti gli identificatori hanno lo stesso tipo delle espressioni che stiamo assegnando (il get di getExpressions() è quello dell'arraylist)
            assignOp.getIds().forEach(id -> {
                String idType = id.getType().replace("ref ", "");
                String exprType = assignOp.getExpressions().get(assignOp.getIds().indexOf(id)).getType().replace("ref ", "");
                // Se uè un assegnamento ad un double di un int o di un int ad un double va bene altrimenti errore
                boolean b_double = !(idType.equals("double") && exprType.equals("int"));
                boolean b_int = !(idType.equals("int") && exprType.equals("double"));
                if (!Objects.equals(idType, exprType) && b_double && b_int) {
                    System.err.println("Conflicting types in assignment: id " + id.getIdentifier() + " has type " + idType + " but expression has type " + exprType);
                    System.exit(1);
                }
            });
            assignOp.setType("notype");
        } else if (statOp instanceof ReadOp readOp)
            readOp.getIds().forEach(id -> id.accept(this));
        else if (statOp instanceof WriteOp writeOp)
            writeOp.getExpressions().forEach(exprOp -> {
                exprOp.accept(this);
                if (exprOp instanceof CallOp callOp && callOp.getType().equals("notype")) {
                    System.err.print("Invalid use of void expression in write operation");
                    System.exit(1);
                }
            });
        else if (statOp instanceof ReturnOp returnOp) {
            // Controlla che non sia presente un return statement in un procedura
            if (funType.equals("void")) {
                System.err.print("Return statement in a procedure");
                System.exit(1);
            }
            returnOp.getExpr().accept(this);
            // Controlla che il tipo dell'espressione sia lo stesso tipo di ritorno della funzione
            String returnType = returnOp.getExpr().getType();
            if (!Objects.equals(returnType, funType)) {
                returnOp.setType("error");
                System.err.print("Invalid return type " + returnType + " for function " + funType);
                System.exit(1);
            }
            returnOp.setType(funType);
        }

    }

    @Override
    public void visit(VarDeclOp varDeclOp) {
        if (varDeclOp.getTypeorconstant() instanceof Constant c)
            c.accept(this);
        if (varDeclOp.getVarOptInitOps() != null)
            varDeclOp.getVarOptInitOps().forEach(varOptInitOp -> varOptInitOp.accept(this));
    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {
        if (varOptInitOp.getExprOp() != null)
            varOptInitOp.getExprOp().accept(this);
    }

    private String extractParameters(String type) {
        return type.substring(type.indexOf("(") + 1, type.indexOf(")"));
    }

    private String extractType(String type) {
        return type.substring(0, type.indexOf("("));
    }

}
