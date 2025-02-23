package utils;

import java.util.HashMap;
import java.util.Map;

public class UnaryOpTable {
    public static final Map<Map<String, String>, String> unaryTable;

    static {
        Map<Map<String, String>, String> tempTable = new HashMap<>();
        addEntry(tempTable, "-", "int", "int");
        addEntry(tempTable, "-", "double", "double");
        addEntry(tempTable, "not", "bool", "bool");
        unaryTable = tempTable;
    }

    private static void addEntry(Map<Map<String, String>, String> table, String operator, String operand, String result) {
        table.put(Map.of(operator, operand), result);
    }

    public static String getResult(String operator, String operand) {
        return unaryTable.get(Map.of(operator, operand));
    }
}
