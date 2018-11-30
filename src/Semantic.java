
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static javafx.scene.input.KeyCode.T;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Manuel Perez Garrido
 */
public class Semantic {

    private ArrayList<String> code;

    public Semantic(ArrayList code) {
        this.code = code;
        runCode(0, code.size());
    }

    public void runCode(int start, int end) {

        for (int i = start; i < end; i++) {
            switch (code.get(i)) {
                case "mientras": {
                    i++;
                    while (code.get(i).equals("(")) {
                        i++;
                    }
                    int variable1 = Integer.parseInt(code.get(i));
                    String condicion = code.get(i + 1);
                    int variable2 = Integer.parseInt(code.get(i + 2));
                    i += 3;
                    switch (condicion) {
                        case "==":
                            if (variable1 == variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                while (variable1 == variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        case ">=":
                            if (variable1 >= variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                while (variable1 >= variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        case "<=":
                            if (variable1 <= variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                while (variable1 <= variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        case ">":
                            if (variable1 > variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                while (variable1 > variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        case "<":
                            if (variable1 < variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                while (variable1 < variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        default:
                            break;
                    }
                }
                break;
                case "si": {
                    i++;
                    while (code.get(i).equals("(")) {
                        i++;
                    }
                    int variable1 = Integer.parseInt(code.get(i));
                    String condicion = code.get(i + 1);
                    int variable2 = Integer.parseInt(code.get(i + 2));
                    i += 3;
                    switch (condicion) {
                        case "==":
                            if (variable1 == variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                if (variable1 == variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        case ">=":
                            if (variable1 >= variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                if (variable1 >= variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        case "<=":
                            if (variable1 <= variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                if (variable1 <= variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        case ">":
                            if (variable1 > variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                if (variable1 > variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        case "<":
                            if (variable1 < variable2) {
                                i = findStart(i);
                                end = findEnd(i);
                                if (variable1 < variable2) {
                                    runCode(i, end);
                                }
                                i = end;
                            }
                            break;
                        default:
                            break;
                    }
                }
                break;
                case "repetir": {
                    int repeticiones = Integer.parseInt(code.get(i + 1));
                    i = findStart(i);
                    end = findEnd(i);
                    for (int iter = 0; iter < repeticiones; iter++) {
                        runCode(i, end);
                    }
                    i = end;
                }
                break;
                case "-": {

                }
                break;
            }
        }
    }

    private int findEnd(int start) {
        int end = 0, endCounter = 0;
        for (int i = start; i < code.size(); i++) {
            switch (code.get(i)) {
                case "{":
                    endCounter++;
                    break;
                case "}": {
                    if (endCounter == 1) {
                        end = i;
                        i = code.size(); // termina ciclo
                    } else {
                        endCounter--;
                    }
                }
                break;
            }
        }
        return end;
    }

    private int findStart(int i) {
        while (!code.get(i).equals("{")) {
            i++;
        }
        return i;
    }
}
