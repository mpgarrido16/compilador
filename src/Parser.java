
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Manuel
 */
public class Parser {

    private ArrayList<String> code;
    private ArrayList<String> code_syntactic; // This is for run the automata
    private ArrayList<String> list_errors = new ArrayList<>();
    private ArrayList<String> reserved_words;
    private ArrayList<HashMap> variables = new ArrayList<>(); // This is for save all variables
    public ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>(); // This is save for save all arrays
    public ArrayList<String> array_name = new ArrayList<>(); // This is for save the names of all arrays

    private final Automaton automata;
    private String alphabet;
    private Boolean encontrado = false;
    private Stack<String> llaves = new Stack<String>();
    private Stack<String> parentesis = new Stack<String>();

    public Parser(ArrayList code, Automaton a, ArrayList rw) {
        this.code = code;
        this.automata = a;
        this.reserved_words = rw;
        code.add("λ");
        findVariables();
        runAutomaton((String) code.get(0), 0, 0);
    }

    public void findVariables() {
        code_syntactic = new ArrayList<>();
        code.forEach((token) -> {
            code_syntactic.add(token);
        });
        Boolean match = false;
        HashMap<String, String> values;
        ArrayList<String> inner;
        for (int i = 0; i < code_syntactic.size(); i++) {
            for (int j = 0; j < reserved_words.size(); j++) {
                if (code_syntactic.get(i).equals(reserved_words.get(j))) {
                    match = true;
                    break;
                }
            }
            if (match == true) {
                match = false;
            } else {
                // Is a variable or a array?
                if (!code_syntactic.get(i + 2).equals("{")) {
                    if (code_syntactic.get(i + 1).equals("=")) {
                        // This is a variable
                        values = new HashMap<>();
                        // Is an integer?
                        if (isNumeric(code_syntactic.get(i + 2))) {
                            code_syntactic.set(i, "var"); // replace the name of the variable
                            code_syntactic.set(i + 2, "n"); // replace the value for the automata
                            values.put(code.get(i), code.get(i + 2));
                            variables.add(values);
                            i += 2;
                        } else {
                            // variable is not integer
                            list_errors.add(code_syntactic.get(i + 2));
                        }
                    } else {
                        code_syntactic.set(i, "var");
                    }
                } else {
                    // This is an array
                    // We save the name of the array first
                    array_name.add(code_syntactic.get(i));
                    // replace the name of the array
                    code_syntactic.set(i, "var");
                    i += 3;
                    inner = new ArrayList<>(); // We save the array here
                    while (!code_syntactic.get(i).equals("}")) {
                        if (!code_syntactic.get(i).equals(",")) {
                            // Is an integer?
                            if (isNumeric(code_syntactic.get(i))) {
                                code_syntactic.set(i, "n"); // replace the value for the automata
                                inner.add(code.get(i));
                            } else {
                                // variable is not integer
                                list_errors.add(code_syntactic.get(i));
                            }
                        }
                        i++;
                    }
                    // Save the complete array
                    array.add(inner);
                }
            }

            match = false;
        }

        for (int i = 0; i < code_syntactic.size(); i++) {
            if (isNumeric(code_syntactic.get(i))) {
                code_syntactic.set(i, "n");
            }
        }

        System.out.println("Variables");
        for (int i = 0; i < variables.size(); i++) {
            values = variables.get(i);
            Iterator iterator = values.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                String value = values.get(key).toString();
                System.out.println("\t" + key + " = " + value);
            }
        }

        System.out.println("Arreglos");
        for (int i = 0; i < array.size(); i++) {
            System.out.print("\t" + array_name.get(i) + " = ");
            ArrayList<String> aux = array.get(i);
            for (int j = 0; j < aux.size(); j++) {
                System.out.print(aux.get(j) + " ");
            }
            System.out.println();
        }

        System.out.println("CODE SYNTACTIC:");
        code_syntactic.forEach((token) -> {
            System.out.println("\t-> " + token);
        });
    }

    public void runAutomaton(String actual, int state, int index) {
        int row, column;
        System.out.print("Rec[ q" + state + ", " + actual + ", " + index + " ]");
        if (code_syntactic.size() == index) {
            if (state == automata.getFinalState() && parentesis.isEmpty() && llaves.isEmpty()) {
                encontrado = true;
            }
        } else if (index < code_syntactic.size()) {
            row = state;
            for (column = 0; column < reserved_words.size(); column++) {
                if (reserved_words.get(column).equals(actual)) {
                    break;
                }
            }

            ArrayList<String> aux = automata.table[row][column];
            index++;
            try {
                actual = (String) code_syntactic.get(index);
                // Pilas de las llaves y los parentesis
                {
                    if (actual.equals("{")) {
                        llaves.push(actual);
                    } else if (actual.equals("}")) {
                        if (llaves.isEmpty()) {
                            index++;
                        }
                        llaves.pop();
                    }
                    if (actual.equals("(")) {
                        parentesis.push(actual);
                    } else if (actual.equals(")")) {
                        if (parentesis.isEmpty()) {
                            index++;
                        }
                        parentesis.pop();
                    }
                }
            } catch (Exception e) {

            }
            for (int h = 0; h < aux.size(); h++) {
                if (!aux.get(h).equals("-")) {
                    state = Integer.parseInt(aux.get(h));
                    System.out.print(" = q" + state + "\n");
                    runAutomaton(actual, state, index);
                }
            }
        }
    }

    private void sleep(int mls) {
        try {
            Thread.sleep(mls);
        } catch (InterruptedException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean isCorrect() {
        return encontrado;
    }

    private String getSyntaxInput() {
        String syntax = "";
        for (int i = 0; i < code.size(); i++) {
            for (int j = 0; j < reserved_words.size() - 1; j++) {
                if (code.get(i).equals(reserved_words.get(j))) {
                    syntax = syntax + Character.toString(alphabet.charAt(j));
                }
            }
        }
        syntax = syntax + "~";
        return syntax;
    }

    private String getAlphabet() {
        String alph = "";
        Character letter = 'a';
        for (String rw : reserved_words) {
            alph += letter;
            letter++;
        }
        return alph;
    }

    public String getErrors() {
        String errors = "";
        for (int i = 0; i < list_errors.size(); i++) {
            errors = errors + (i + 1) + ": " + list_errors.get(i) + '\n';
        }
        return errors;
    }

    public static boolean isNumeric(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }

}
