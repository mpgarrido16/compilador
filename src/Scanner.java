
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author todoe
 */
public class Scanner {

    ArrayList<String> code_tokens;
    ArrayList<String> list_errors;

    public Scanner(String code) {;
        setCodeTokens(code);
    }

    private Boolean isCharacter(char c) {
        final char eof = (char) -1;
        switch (c) {
            case '\n':
            case '\t':
            case ' ':
            case eof:
                return false;
        }
        return true;
    }

    private Boolean isSymbol(char c) {
        switch (c) {
            case '(':
            case ')':
            case '{':
            case '}':
            case ';':
            case '<':
            case '>':
            case '&':
            case '!':
            case '|':
            case ',':
            case '=':
            case '+':
            case '-':
            case '/':
            case '*':
                return true;
        }
        return false;
    }

    private Boolean isOperatorSuffix(String suffix) {
        switch (suffix) {
            case "=":
            case "+":
            case "-":
                return true;
        }
        return false;
    }

    private Boolean isComparatorSuffix(String comparator) {
        switch (comparator) {
            case "<":
            case ">":
                return true;
        }
        return true;
    }

    private Boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private Boolean isInteger(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    ///------------------------------
    private void setOperators() {
        ArrayList<String> auxiliar = new ArrayList<>();
        System.out.println("codigo en tokens");
        for (int i = 0, j = 1; i < code_tokens.size(); i++, j++) {
            if (isOperatorSuffix(code_tokens.get(i)) && j < code_tokens.size()) {
                if (code_tokens.get(i).equals(code_tokens.get(j))) {
                    //  System.out.println(code_tokens.get(i) + code_tokens.get(j));
                    auxiliar.add(code_tokens.get(i) + code_tokens.get(j));
                } else if (code_tokens.get(i).equals("=") && !code_tokens.get(j).equals("=") && !code_tokens.get(i - 1).equals("=")) {
                    auxiliar.add(code_tokens.get(i));
                }
            } else if (code_tokens.get(i).equals(">") && code_tokens.get(j).equals("=")) {
                // System.out.println(code_tokens.get(i) + code_tokens.get(j));
                auxiliar.add(code_tokens.get(i) + code_tokens.get(j));
            } else if (code_tokens.get(i).equals("<") && code_tokens.get(j).equals("=")) {
                // System.out.println(code_tokens.get(i) + code_tokens.get(j));
                auxiliar.add(code_tokens.get(i) + code_tokens.get(j));
            } else {
                auxiliar.add(code_tokens.get(i));
                // System.out.println(code_tokens.get(i));
            }
        }
        code_tokens = auxiliar;
    }

    private void setCodeTokens(String code) {
        String token = ""; // String of words
        Character character; // Character of the string
        code_tokens = new ArrayList<>(); // List of tokens 

        for (int c = 0; c < code.length(); c++) {
            character = code.charAt(c);
            if (isCharacter(character)) {
                if (!isDigit(character)) {
                    if (isSymbol(character)) {
                        if (!token.isEmpty()) {
                            code_tokens.add(token);
                            code_tokens.add(Character.toString(character));
                            token = "";
                        } else {
                            code_tokens.add(Character.toString(character));
                        }
                    } else {
                        token += Character.toString(character);
                    }
                } else {
                    // Search the numerical values
                    if (token.isEmpty()) {
                        while (isDigit(character)) {
                            token += Character.toString(character);
                            c++;
                            character = code.charAt(c);
                        }
                        c--;
                        code_tokens.add(token);
                        token = "";
                    } else {
                        /**
                         * If there are letter behind Example var122
                         */
                        token += character;
                    }
                }
            } else {
                if (!token.isEmpty()) {
                    code_tokens.add(token);
                }
                token = "";
            }
        }
        setOperators();
    }

    public ArrayList<String> getCodeTokens() {
        return code_tokens;
    }

    public void printCodeInTokens() {
        System.out.println("CODE:");
        code_tokens.forEach((token) -> {
            System.out.println("\t-> " + token);
        });
    }
}
