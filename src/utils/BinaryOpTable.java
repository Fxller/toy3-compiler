package utils;

import java.util.HashMap;
import java.util.Map;

public class BinaryOpTable {
    public static final Map<Map<String, Pair>, String> binaryTable;

    static {
        Map<Map<String, Pair>, String> tempTable = new HashMap<>();;
        addEntry(tempTable, "+", "int", "int", "int");
        addEntry(tempTable, "+", "double", "double", "double");
        addEntry(tempTable, "+", "int", "double", "double");
        addEntry(tempTable, "+", "double", "int", "double");
        addEntry(tempTable, "-", "int", "int", "int");
        addEntry(tempTable, "-", "double", "double", "double");
        addEntry(tempTable, "-", "int", "double", "double");
        addEntry(tempTable, "-", "double", "int", "double");
        addEntry(tempTable, "*", "int", "int", "int");
        addEntry(tempTable, "*", "double", "double", "double");
        addEntry(tempTable, "*", "int", "double", "double");
        addEntry(tempTable, "*", "double", "int", "double");
        addEntry(tempTable, "/", "int", "int", "int");
        addEntry(tempTable, "/", "double", "double", "double");
        addEntry(tempTable, "/", "int", "double", "double");
        addEntry(tempTable, "/", "double", "int", "double");
        addEntry(tempTable, "+", "string", "string", "string");
        addEntry(tempTable, "+", "int", "string", "string");
        addEntry(tempTable, "+", "string", "int", "string");
        addEntry(tempTable, "+", "double", "string", "string");
        addEntry(tempTable, "+", "string", "double", "string");
        addEntry(tempTable, "and", "bool", "bool", "bool");
        addEntry(tempTable, "or", "bool", "bool", "bool");
        addEntry(tempTable, ">", "int", "int", "bool");
        addEntry(tempTable, ">", "double", "double", "bool");
        addEntry(tempTable, ">", "int", "double", "bool");
        addEntry(tempTable, ">", "double", "int", "bool");
        addEntry(tempTable, "<", "int", "int", "bool");
        addEntry(tempTable, "<", "double", "double", "bool");
        addEntry(tempTable, "<", "int", "double", "bool");
        addEntry(tempTable, "<", "double", "int", "bool");
        addEntry(tempTable, ">=", "int", "int", "bool");
        addEntry(tempTable, ">=", "double", "double", "bool");
        addEntry(tempTable, ">=", "int", "double", "bool");
        addEntry(tempTable, ">=", "double", "int", "bool");
        addEntry(tempTable, "<=", "int", "int", "bool");
        addEntry(tempTable, "<=", "double", "double", "bool");
        addEntry(tempTable, "<=", "int", "double", "bool");
        addEntry(tempTable, "<=", "double", "int", "bool");
        addEntry(tempTable, "==", "int", "int", "bool");
        addEntry(tempTable, "==", "double", "double", "bool");
        addEntry(tempTable, "==", "int", "double", "bool");
        addEntry(tempTable, "==", "double", "int", "bool");
        addEntry(tempTable, "==", "string", "string", "bool");
        addEntry(tempTable, "<>", "int", "int", "bool");
        addEntry(tempTable, "<>", "double", "double", "bool");
        addEntry(tempTable, "<>", "int", "double", "bool");
        addEntry(tempTable, "<>", "double", "int", "bool");
        addEntry(tempTable, "<>", "string", "string", "bool");
        binaryTable = tempTable;
    }

    private static void addEntry(Map<Map<String, Pair>, String> table, String operator, String type1, String type2, String result) {
        table.put(Map.of(operator, new Pair(type1, type2)), result);
    }

    public static String getResult(String operator, String type1, String type2) {
        return binaryTable.get(Map.of(operator, new Pair(type1, type2)));
    }

}
