
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Manuel
 */
public final class Automaton {

    public ArrayList<String> table[][];
    private int statesSize;
    private int alphabetSize;
    private int finalState;
    private int numberOfFinalStates;

    public Automaton(String route, int alphabetSize) {
        this.alphabetSize = alphabetSize + 1;
        File file;
        FileReader fr = null;
        BufferedReader br;
        Character character;
        String line, state;
        int j, k;
        try {
            file = new File(route);
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            line = br.readLine();
            setStatesSize(Integer.parseInt(line));
            setTable(statesSize, alphabetSize);

            System.out.println("statesSize: " + statesSize);
            System.out.println("alphabetSize: " + alphabetSize);

            for (int i = 0; i < statesSize; i++) {
                line = br.readLine();
                state = "";
                j = 0;
                k = 0;
                while (j < line.length()) {
                    character = line.charAt(j);
                    switch (character) {
                        case '{':
                            j++;
                            break;
                        case '}':
                            table[i][k].add(state);
                            //System.out.print(state + "\t");
                            state = "";
                            j++;
                            k++;
                            break;
                        case ',':
                            table[i][k].add(state);
                            // System.out.print(state + " ");
                            state = "";
                            j++;
                            break;
                        case ' ':
                            j++;
                            break;
                        default:
                            character = line.charAt(j);
                            state += Character.toString(character);
                            j++;
                            break;
                    }
                }

            }
            // printAutomaton();
            numberOfFinalStates = Integer.parseInt(br.readLine());
            finalState = Integer.parseInt(br.readLine());
            System.out.println("numberOfFinalStates: " + numberOfFinalStates);
            System.out.println("finalState: " + finalState);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            // Close the file
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void printAutomaton() {
        for (ArrayList<String>[] table1 : table) {
            for (int j = 0; j < table[0].length; j++) {
                for (int z = 0; z < table1[j].size(); z++) {
                    System.out.print(table1[j].get(z) + " ");
                }
                if (table1[j].size() <= 1) {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }
    }

    public ArrayList<String>[][] getTable() {
        return table;
    }

    public void setTable(int statesSize, int alphabetSize) {
        table = new ArrayList[statesSize][alphabetSize];
        // Initializing ArrayList
        for (int i = 0; i < statesSize; i++) {
            table[i] = new ArrayList[alphabetSize];
            for (int z = 0; z < alphabetSize; z++) {
                table[i][z] = new ArrayList<>();
            }
        }
    }

    public int getStatesSize() {
        return statesSize;
    }

    public void setStatesSize(int statesSize) {
        this.statesSize = statesSize;
    }

    public int getAlphabetSize() {
        return alphabetSize;
    }

    public void setAlphabetSize(int alphabetSize) {
        this.alphabetSize = alphabetSize;
    }

    public int getFinalState() {
        return finalState;
    }

    public int getNumberOfFinalStates() {
        return numberOfFinalStates;
    }

}
