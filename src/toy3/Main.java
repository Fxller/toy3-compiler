package toy3;

import nodes.program.ProgramOp;
import visitors.PrintVisitor;
import visitors.CodeGenerator;
import visitors.Scoping;
import visitors.TypeChecking;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

// TODO: valid3 non stampa quello che dovrebbe, nella seconda stampa di message nella funzione scoping
//  deve stampare level 1 e non level 3.1 (controllare sempre i passaggi per rifimento)
// TODO: valid4 potrebbe non andare nella continous integration in quanto mancano le è e c'è uno spazio in più prima della stampa finale di sommagrande, in caso controllare

public class Main {
//    private static final String[] files = {"valid1.txt", "valid2.txt", "valid3.txt", "valid4.txt"}; //, "test_program.txt", "valid1.txt", "valid2.txt", "valid3.txt", "valid4.txt"};

    public static void main(String[] args) {
        // TODO: fixare valid4
//        for (int i = 0; i < files.length; i++) {
            Path inputPath = Paths.get(args[0]);
            String inputFileName = inputPath.toString();
            String outputFileName = inputPath.getFileName().toString().replace(".txt", "");
            try (Reader fileReader = new BufferedReader(new FileReader(inputFileName))) {
//                System.out.println("File: " + files[i]);
                Lexer lexer = new Lexer(fileReader);
                parser p = new parser(lexer);
                try {
                    // Esegui il parsing per costruire l'AST
                    ProgramOp ast = (ProgramOp) p.parse().value;

                    // Applica il Visitor all'AST
                    // System.out.println("** Stampa dell'AST **");
                    PrintVisitor printVisitor = new PrintVisitor();
                    ast.accept(printVisitor);

                    // Test the Scoping visitor
                    // System.out.println("** Testing Scoping Visitor **");
                    Scoping scopingVisitor = new Scoping();
                    ast.accept(scopingVisitor);

                    // System.out.println("** Testing TypeChecking Visitor **");
                    TypeChecking typeChecking = new TypeChecking();
                    ast.accept(typeChecking);

                    // System.out.println("** Testing Code Generation **");
                    CodeGenerator codeGenerator = new CodeGenerator(outputFileName);
                    ast.accept(codeGenerator);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    }
}

