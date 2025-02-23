package visitors;

import nodes.body.BodyOp;
import nodes.defdecl.DefDeclOp;
import nodes.expr.*;
import nodes.pardecl.PVarOp;
import nodes.pardecl.ParDeclOp;
import nodes.program.BeginEndOp;
import nodes.program.ProgramOp;
import nodes.stat.*;
import nodes.vardecl.VarDeclOp;
import nodes.vardecl.VarOptInitOp;
import interfaces.Visitor;
import nodes.funcall.CallOp;

import java.io.*;
import java.util.*;

// TODO: cambiare la grammatica (ArithOp, BoolOp ecc.)
// TODO: java va in loop brutti e cattivi

public class CodeGenerator implements Visitor {
    private final StringBuilder code;
    private final BufferedWriter bufferedWriter;
    private static int counter = 1;

    private final Map<String, String> funType = new HashMap<>();
    private List<ExprOp> constants = new ArrayList<>();
    private boolean isCallOpInWrite = false;
    private int ring_index = 0;

    public CodeGenerator(String fileName) throws IOException {
        code = new StringBuilder();
        bufferedWriter = new BufferedWriter(new FileWriter("test_files/c_out/"+ fileName + ".c"));
    }

    @Override
    public void visit(ProgramOp programOp) {
        code.append("#include <stdio.h>\n");
        code.append("#include <stdbool.h>\n");
        code.append("#include <string.h>\n");
        code.append("#include <stdlib.h>\n\n");
        code.append("#define N 5000\n");
        code.append("char _buffer[N] = \"\";\n");
        code.append("char _tmp[10][N];\n\n");

        // Lista di funzioni per controllare che non ci siano funzioni con lo stesso nome e
        // anche nella CallOp in caso si dovesse aggiungere il passaggio per riferimento
        for (Object obj : programOp.getVarDeclOpsordefDeclOps()) {
            if (obj instanceof DefDeclOp defDeclOp)
                funType.put(defDeclOp.getId().getIdentifier(), defDeclOp.getId().getType());
        }

        // Aggiunta delle firme delle funzioni in testa al file
        for (Object obj : programOp.getVarDeclOpsordefDeclOps()) {
            if (obj instanceof DefDeclOp defDeclOp) {
                StringJoiner stringJoiner = new StringJoiner(", ");

                String typeFun = defDeclOp.getType();
                // In caso la funzine restituisca tipo string viene cambiato con char*
                if (typeFun.equals("string"))
                    typeFun = "char*";

                code.append(typeFun).append(" ").append(defDeclOp.getId().getIdentifier());
                code.append("(");
                if (defDeclOp.getParDecls() != null)
                    defDeclOp.getParDecls().forEach(parDeclOp -> parDeclOp.getPVars().forEach(pVarOp -> {
                        String type = parDeclOp.getType().equals("string") ? "char*" : parDeclOp.getType();
                        // Se c'è ref nella definizione della funzione aggiunge * alla variabile
                        if (funType.containsKey(pVarOp.getId().getIdentifier())) {
                            pVarOp.getId().setIdentifier("_" + pVarOp.getId().getIdentifier());
                        }
                        if (pVarOp.getRef()) {
                            // Se getRef() è true e il tipo è string non aggiungne l'asterisco
                            // Se getRef() è true e il tipo non è string aggiunge l'asterisco
                            String ref = pVarOp.getRef() ? (parDeclOp.getType().equals("string")) ? " " : "* " : " ";
                            stringJoiner.add(type + ref + pVarOp.getId().getIdentifier());
                        } else
                            stringJoiner.add(type + " " + pVarOp.getId().getIdentifier());
                    }));

                code.append(stringJoiner);
                code.append(");\n");

            }
        }

        // Dichiarazione delle variabili globali sotto alle firme delle funzioni
        for (Object obj : programOp.getVarDeclOpsordefDeclOps()) {
            if (obj instanceof VarDeclOp)
                ((VarDeclOp) obj).accept(this);
        }

        code.append("\n");
        programOp.getBeginEndOp().accept(this);
        code.append("\n\n");

        // Scrittura del corpo delle funzioni sotto al main
        for (Object obj : programOp.getVarDeclOpsordefDeclOps()) {
            if (obj instanceof DefDeclOp) {
                ((DefDeclOp) obj).accept(this);
                code.append("\n");
            }
        }

        try {
            bufferedWriter.append(code.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void visit(DefDeclOp defDeclOp) {
        StringJoiner stringJoiner = new StringJoiner(", ");

        // In caso la funzione sia di tipo string viene stampato char*
        String typeFun = defDeclOp.getType();
        if (typeFun.equals("string"))
            typeFun = "char*";

        code.append(typeFun).append(" ").append(defDeclOp.getId().getIdentifier());
        code.append("(");

        // Append dei parametri della funzione con controllo per il passaggio per riferimento e se è una stringa
        if (defDeclOp.getParDecls() != null) {
            defDeclOp.getParDecls().forEach(parDeclOp -> parDeclOp.getPVars().forEach(pVarOp -> {
                String type = parDeclOp.getType().equals("string") ? "char*" : parDeclOp.getType();
                // Controlla se un parametro ha lo stesso tipo di una funzione dichiarata
                // perchè nelle variaibli lo fa in automatico da Identifier
                if (funType.containsKey(pVarOp.getId().getIdentifier())) {
                    pVarOp.getId().setIdentifier("_" + pVarOp.getId().getIdentifier());
                }
                if (pVarOp.getRef()) {
                    String ref = pVarOp.getRef() ? (parDeclOp.getType().equals("string")) ? " " : "* " : " ";
                    stringJoiner.add(type + ref + pVarOp.getId().getIdentifier());
                } else
                    stringJoiner.add(type + " " + pVarOp.getId().getIdentifier());
            }));
        }

        code.append(stringJoiner);
        code.append(") {\n");

        defDeclOp.getBody().accept(this);

        code.append("}\n");
    }


    @Override
    public void visit(ParDeclOp parDeclOp) {

    }

    @Override
    public void visit(PVarOp pVarOp) {
        pVarOp.getId().accept(this);
    }

    @Override
    public void visit(BodyOp bodyOp) {
        bodyOp.getVarDecls().forEach(varDeclOp -> varDeclOp.accept(this));

        bodyOp.getStats().forEach(statOp -> {
            statOp.accept(this);
            if (statOp instanceof CallOp)
                code.append(";\n");
        });
    }

    @Override
    public void visit(BeginEndOp beginEndOp) {
        code.append("int main(void) {\n");
        beginEndOp.getVarDeclOps().forEach(varDeclOp -> varDeclOp.accept(this));
        beginEndOp.getStatOps().forEach(statOp -> {
            statOp.accept(this);
            if (statOp instanceof CallOp)
                code.append(";\n");
        });
        code.append("return 0;\n}");
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {
        varDeclOp.getVarOptInitOps().forEach(varOptInitOp -> varOptInitOp.accept(this));
    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {
        String type = varOptInitOp.getId().getType();
        // Nel caso in cui la variabile sia di tipo string la dichiara come un array di char
        if (type.equals("string")) {
            code.append("char ");
            varOptInitOp.getId().accept(this);
            code.append("[N]");
            // Se e' gia' inizializzata fa l'accept dell'espressione, altrimenti la inizializza a ""
        } else {
            code.append(type).append(" ");
            varOptInitOp.getId().accept(this);
        }
        if (varOptInitOp.getExprOp() != null) {
            code.append(" = ");
            varOptInitOp.getExprOp().accept(this);
        }
        code.append(";\n");
    }

    @Override
    public void visit(CallOp callOp) {
        if (callOp.getArguments() != null) {
            isConcatInCall(callOp.getArguments());
        }

        // Append della chiamata a funzione
        if (!isCallOpInWrite) {
            String id = callOp.getId().getIdentifier();

            code.append(id);
            code.append("(");
            String type = funType.get(id);
            String[] types = type.substring(type.indexOf("(") + 1, type.indexOf(")")).split(",");

            if (callOp.getArguments() != null) {
                for (int i = 0; i < callOp.getArguments().size(); i++) {
                    // Se c'e' ref e il tipo non e' string aggiunge &
                    if (types[i].contains("ref") && !types[i].contains("string") && !callOp.getArguments().get(i).getType().contains("ref"))
                        code.append("&");
                    // In caso ci sia un passaggio per riferimento, non append * alla variabile
                    if (callOp.getArguments().get(i) instanceof Identifier identifier)
                        code.append(identifier.getIdentifier());
                    else
                        callOp.getArguments().get(i).accept(this);

                    if (i < callOp.getArguments().size() - 1) {
                        code.append(", ");
                    }
                }
            }
            code.append(")");
        }
    }


    @Override
    public void visit(ExprOp exprOp) {
        // Se e' una stringa constante aggiunge i doppi apici
        if (exprOp instanceof Constant constant) {
            if (constant.getType().equals("string")) {
                code.append("\"");
                code.append(escapeStringForC((String) constant.getValue()));
                code.append("\"");
            } else
                code.append(constant.getValue());
        } else if (exprOp instanceof Identifier identifier) {
            // Se è presente una funzone con lo stesso nome viene aggiunto "_" davanti al nome
            if (funType.containsKey(identifier.getIdentifier())) {
                identifier.setIdentifier("_" + identifier.getIdentifier());
            }
            // Aggiunta dello * in caso sia una variabile di tipo ref
            if (identifier.getType().contains("ref") && !identifier.getType().contains("string")) {
                code.append("(");
                code.append("*");
                code.append(identifier.getIdentifier());
                code.append(")");
            } else
                code.append(identifier.getIdentifier());
        } else if (exprOp instanceof BinaryExprOp binaryExprOp) {
            //  Controlla se c'è una concatenazione di stringhe in una CallOp
            if (binaryExprOp.getLeft() instanceof CallOp callOp) {
                isConcatInCall(callOp.getArguments());
                // isCallOpInWrite = true;
//                :/:
                // isCallOpInWrite = false;
            }
            if (binaryExprOp.getRight() instanceof CallOp callOp) {
                isConcatInCall(callOp.getArguments());
                // isCallOpInWrite = true;
                // isCallOpInWrite = false;
            }

            // Controlla se e' una concatenazione di stringhe
            if (isStringConcat(binaryExprOp)) {
                // Chiamata della funzione ricorsiva per prendere gli argomenti della snprintf
                strConcat(binaryExprOp);
                code.append("snprintf(_buffer, N, \"");
                for (ExprOp e : constants) {
                    String type = e.getType();
                    switch (type) {
                        case "string" -> code.append("%s");
                        case "char", "ref char" -> code.append("%c");
                        case "int", "bool", "ref int", "ref bool" -> code.append("%d");
                        case "double", "ref double" -> code.append("%lf");
                    }
                }
                code.append("\", ");
                // Scrive i vari parametri della concatenazione nella snprintf, separati da virgola
                for (ExprOp e1 : constants) {
                    e1.accept(this);
                    if (constants.indexOf(e1) < constants.size() - 1)
                        code.append(", ");
                }
                code.append(")");
                constants.clear();

                // Controllo su se l'operazione e' una comparazione di stringhe
            } else if ((binaryExprOp.getOperator().equals("==") || binaryExprOp.getOperator().equals("<>"))
                    && binaryExprOp.getLeft().getType().equals("string") && binaryExprOp.getRight().getType().equals("string")) {
                code.append("strcmp((");
                binaryExprOp.getLeft().accept(this);
                code.append("), (");
                binaryExprOp.getRight().accept(this);
                code.append(")");
                if (binaryExprOp.getOperator().equals("<>"))
                    code.append(") != 0");
                else
                    code.append(") == 0");
            } else {
                // Effettua l'operazione binaria tra due espressioni
                code.append("(");
                binaryExprOp.getLeft().accept(this);
                code.append(") ");
                code.append(binaryExprOp.getCOperator(binaryExprOp.getOperator()));
                code.append(" (");
                binaryExprOp.getRight().accept(this);
                code.append(")");
            }

            // Espressione unaria
        } else if (exprOp instanceof UnaryExprOp unaryExprOp) {
            code.append(unaryExprOp.getCOperator(unaryExprOp.getOperator()));
            code.append("(");
            unaryExprOp.getExpr().accept(this);
            code.append(")");
        }
    }

    @Override
    public void visit(StatOp statOp) {
        if (statOp instanceof CallOp callOp) {
            callOp.accept(this);
            code.append(";\n");
        }
        else if (statOp instanceof IfThenOp ifThenOp) {
            if (ifThenOp.getExpr() instanceof BinaryExprOp binaryExprOp) {
                binaryExprOp.setLeft(isConcatInCall(binaryExprOp.getLeft()));
                if (binaryExprOp.getLeft() instanceof CallOp callOp) {
                    isConcatInCall(callOp.getArguments());
                }
                binaryExprOp.setRight(isConcatInCall(binaryExprOp.getRight()));
                if (binaryExprOp.getRight() instanceof CallOp callOp) {
                    isConcatInCall(callOp.getArguments());
                }
            }

            if (ifThenOp.getExpr() instanceof CallOp callOp)
                isConcatInCall(callOp.getArguments());

            code.append("if (");
            ifThenOp.getExpr().accept(this);
            code.append(") {\n");
            ifThenOp.getBody().accept(this);
            code.append("}\n");

        } else if (statOp instanceof IfThenElseOp ifThenElseOp) {
            if (ifThenElseOp.getExpr() instanceof BinaryExprOp binaryExprOp) {
                binaryExprOp.setLeft(isConcatInCall(binaryExprOp.getLeft()));
                if (binaryExprOp.getLeft() instanceof CallOp callOp)
                    isConcatInCall(callOp.getArguments());
                binaryExprOp.setRight(isConcatInCall(binaryExprOp.getRight()));
                if (binaryExprOp.getRight() instanceof CallOp callOp)
                    isConcatInCall(callOp.getArguments());
            }
            if (ifThenElseOp.getExpr() instanceof CallOp callOp)
                isConcatInCall(callOp.getArguments());

            code.append("if (");
            ifThenElseOp.getExpr().accept(this);
            code.append(") {\n");
            ifThenElseOp.getThenBody().accept(this);
            code.append("} else {\n");
            ifThenElseOp.getElseBody().accept(this);
            code.append("}\n");

        } else if (statOp instanceof AssignOp assignOp) {
            isConcatInCall(assignOp.getExpressions());
            for (int i = 0; i < assignOp.getIds().size(); i++) {
                ExprOp e = assignOp.getExpressions().get(i);
                //Controlla se stiamo assegnando una CallOp che all'interno ha una concatenazione di stringhe
                if (e instanceof CallOp callOp && e.getType().equals("string")) {
                    // isCallOpInWrite = true;
                    isConcatInCall(callOp.getArguments());
                    // isCallOpInWrite = false;

                    code.append("strncpy(");
                    assignOp.getIds().get(i).accept(this);
                    code.append(", ");
                    e.accept(this);
                    code.append(", N-1)");
                    // Controlla se stiamo assegnando una stringa a una variabile di tipo string
                } else if ((e instanceof Identifier && e.getType().equals("string"))
                        || (e instanceof Constant && e.getType().equals("string"))) {
                    code.append("strncpy(");
                    assignOp.getIds().get(i).accept(this);
                    code.append(", ");
                    e.accept(this);
                    code.append(", N-1)");
                    // Controlla se stiamo assegnando una concatenazione di stringhe
//                } else if (isStringConcat(e)) {
//                    e.accept(this);
////                    code.append(", strncpy(").append(assignOp.getIds().get(i).getIdentifier()).append(" , _buffer, N-1), ");
////                    code.append(assignOp.getIds().get(i).getIdentifier()).append("[N-1] = '\\0'");
//                    code.append("strncpy(");
//                    assignOp.getIds().get(i).accept(this);
//                    code.append(", ");
//                    e.accept(this);
//                    code.append(", N-1)");
//                } else {
                }else {
                    Identifier id = assignOp.getIds().get(i);
                    id.accept(this);
                    code.append(" = ");
                    e.accept(this);
                    if (i < assignOp.getIds().size() - 1)
                        code.append(", ");
                }
            }
            // Append del ;\n nel caso in cui non ci sia una callOp come espressione
            if (assignOp.getExpressions().stream().anyMatch(exprOp -> !(exprOp instanceof CallOp)))
                code.append(";\n");
            else if (assignOp.getExpressions().size() == 1 && assignOp.getExpressions().get(0) instanceof CallOp)
                code.append(";\n");
        } else if (statOp instanceof ReadOp readOp) {
            code.append("scanf(\"");
            for (int i = 0; i < readOp.getIds().size(); i++) {
                switch (readOp.getIds().get(i).getType()) {
                    case "string" -> code.append("%s");
                    case "char", "ref char" -> code.append("%c");
                    case "int", "bool", "ref int", "ref bool" -> code.append("%d");
                    case "double", "ref double" -> code.append("%lf");
                }
                if (i < readOp.getIds().size() - 1)
                    code.append(" ");
            }
            code.append("\"");
            // Aggiunge & alla variabili che non sono di tipo string (char*)
            for (Identifier id : readOp.getIds()) {
                if (id.getType().equals("string")) {
                    code.append(", ");
                    id.accept(this);
                } else {
                    code.append(", &");
                    id.accept(this);
                }
            }
            code.append(");\n");
        } else if (statOp instanceof WriteOp writeOp) {
            for (int i = 0; i < writeOp.getExpressions().size(); i++) {
                if (writeOp.getExpressions().get(i) instanceof CallOp callOp) {
                      isConcatInCall(callOp.getArguments());
                }
                // Utilizzo di una variabile temporanea nel caso in cui bisogna stampare la concatenazione di stringhe
                writeOp.getExpressions().set(i, isConcatInCall(writeOp.getExpressions().get(i)));
            }

            code.append("printf(\"");
            for (int i = 0; i < writeOp.getExpressions().size(); i++) {
                switch (writeOp.getExpressions().get(i).getType()) {
                    case "string", "ref string" -> code.append("%s");
                    case "char", "ref char" -> code.append("%c");
                    case "int", "bool", "ref int", "ref bool" -> code.append("%d");
                    case "double", "ref double" -> code.append("%lf");
                }
                if (i < writeOp.getExpressions().size() - 1)
                    code.append(" ");
            }

            if (writeOp.getNewLine())
                code.append("\\n");
            code.append("\", ");

            for (int i = 0; i < writeOp.getExpressions().size(); i++) {
                writeOp.getExpressions().get(i).accept(this);
                if (i < writeOp.getExpressions().size() - 1)
                    code.append(", ");
            }
            code.append(");\n");

        } else if (statOp instanceof WhileOp whileOp) {
            if (whileOp.getExpr() instanceof BinaryExprOp binaryExprOp) {
                binaryExprOp.setLeft(isConcatInCall(binaryExprOp.getLeft()));
                if (binaryExprOp.getLeft() instanceof CallOp callOp)
                    isConcatInCall(callOp.getArguments());

                binaryExprOp.setRight(isConcatInCall(binaryExprOp.getRight()));
                if (binaryExprOp.getRight() instanceof CallOp callOp)
                    isConcatInCall(callOp.getArguments());
            }

            if (whileOp.getExpr() instanceof CallOp callOp)
                isConcatInCall(callOp.getArguments());

            code.append("while (");
            whileOp.getExpr().accept(this);
            code.append(") {\n");
            whileOp.getBody().accept(this);
            code.append("}\n");

        } else if (statOp instanceof ReturnOp returnOp) {
            if (returnOp.getExpr() instanceof CallOp callOp)
                isConcatInCall(callOp.getArguments());
            returnOp.setExpr(isConcatInCall(returnOp.getExpr()));
            code.append("return ");
            returnOp.getExpr().accept(this);
            code.append(";\n");
        }
    }

    // Funzione ricorsiva per prendere gli argomenti della snprintf dalla SBinaryOp
    private void strConcat(ExprOp exprOp) {
        if (!(exprOp instanceof BinaryExprOp binaryExprOp))
            return;

        if (binaryExprOp.getType().equals("string") && binaryExprOp.getOperator().equals("+")) {
            if (binaryExprOp.getLeft() instanceof Constant || binaryExprOp.getLeft() instanceof Identifier
                    || binaryExprOp.getLeft() instanceof CallOp)
                constants.add(binaryExprOp.getLeft());
            else
                strConcat(binaryExprOp.getLeft());

            if (binaryExprOp.getRight() instanceof Constant || binaryExprOp.getRight() instanceof Identifier
                    || binaryExprOp.getRight() instanceof CallOp)
                constants.add(binaryExprOp.getRight());
            else
                strConcat(binaryExprOp.getRight());
        }
    }

    // Funzione per l'escape delle stringhe in C
    private String escapeStringForC(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "\\t")
                .replace("\r", "\\r");
    }

    private boolean isStringConcat(ExprOp e) {
        if (e instanceof BinaryExprOp binaryExprOp) {
            if (e.getType().equals("string"))
                return binaryExprOp.getOperator().equals("+");

        }
        return false;
    }

    private void isConcatInCall(List<ExprOp> exprs) {
        if (exprs != null)
            for (ExprOp e : exprs) {
                if (e instanceof CallOp callOp) {
                    isConcatInCall(callOp.getArguments());
                }
                if (isStringConcat(e)) {
                    e.accept(this);
                    code.append(", strncpy(_tmp[" + ring_index + "], _buffer, N-1), _tmp[" + ring_index + "][N-1] = '\\0';\n");
                    int i = exprs.indexOf(e);
                    exprs.set(i, new Identifier("_tmp[" + ring_index + "]"));
                    exprs.get(i).setType("string");
                    ring_index = (ring_index + 1) % 10;
                }
            }
    }

    private ExprOp isConcatInCall(ExprOp exprOp) {
        if (exprOp instanceof CallOp callOp) {
            isConcatInCall(callOp.getArguments());
        }
        if (isStringConcat(exprOp)) {
            exprOp.accept(this);
            code.append(", strncpy(_tmp[" + ring_index + "], _buffer, N-1), _tmp[" + ring_index + "][N-1] = '\\0';\n");
            exprOp = new Identifier("_tmp[" + ring_index + "]");
            exprOp.setType("string");
            ring_index = (ring_index + 1) % 10;
        }
        return exprOp;
    }
}