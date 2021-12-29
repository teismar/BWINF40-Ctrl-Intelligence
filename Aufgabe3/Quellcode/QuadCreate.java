import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class QuadCreate {
    public static void main(String[] args) throws Exception {
        System.out.println("Bitte Datei auswählen:");
        JFileChooser chooser = new JFileChooser(); //Datei-Explorer
        chooser.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
        chooser.setMultiSelectionEnabled(false);
        int returnVal = chooser.showSaveDialog(null);
        File f = chooser.getSelectedFile();
        String a = f.getAbsolutePath();
        createWordMatrix(a);
    }

    public static String readLine(String fileLoc, int lin) throws IOException { //Hilfsmethode zum Dateilesen
        String line = Files.readAllLines(Paths.get(fileLoc)).get(lin);
        return line;
    }

    public static void createWordMatrix(String fileP) throws Exception {

        String size = readLine(fileP, 0);
        String[] sp = size.split(" ");
        char[][] matrix = new char[Integer.valueOf(sp[0])][Integer.valueOf(sp[1])];
        System.out.println("Bitte lege eine Delay fest (500 als Standardwert nutzen, falls das Programm keine Ergbenisse liefert in 100er Schritten steigen)");
        Scanner delRe = new Scanner(System.in);
        long delay = delRe.nextLong();
        System.out.println("1 = Horizontal; 2 = Vertikal; 3 = Horizontal und Vertikal; 4 = Wörter können Rückwärts geschrieben sein; 5 = Matrix wird mit Buchstaben aus den gesuchten Wörtern gefüllt)");
        Scanner reader = new Scanner(System.in);
        System.out.println("Bitte einen Schwierigkeitsgrad auswählen (Ein ganzzahliger Wert zwischen 1 und 5)");
        char[][] rdyMat = fillMat(fileP, wordArr(fileP), matrix, reader.nextInt(), delay);
        System.out.println(Arrays.deepToString(wordArr(fileP)));
        printArr(rdyMat);

    }

    public static char[][] fillMat(String fileP, String[] word, char[][] mat, int difC, long delay) throws Exception {
        Random random = new Random();
        int[] dir = new int[]{1, 2}; // 1 - Horizontal; 2 - Vertikal
        String letters = toLetterArr(word);


        long start = System.currentTimeMillis();
        long end = delay;
        String size = readLine(fileP, 0);
        String[] sp = size.split(" ");
        char[][] matrix = new char[Integer.valueOf(sp[0])][Integer.valueOf(sp[1])];
        int dd = 1;
        int back = 1;
        switch (difC) { //Schwierigkeit
            case 4: {
                back = 4;
            }
            break;
            case 5: {
                back = 4;
            }
            break;
        }
        for (int i = 0; i < word.length; i++) {

            switch (difC) {
                case 1: {
                    dd = 2;
                }
                break;
                case 2: {
                    dd = 1;
                }
                break;
                case 3: {
                    int randomD = random.nextInt(dir.length);
                    dd = dir[randomD];
                }
                break;
                default: {
                    word[i] = new StringBuilder(word[i]).reverse().toString();
                    int randomD = random.nextInt(dir.length);
                    dd = dir[randomD];
                }
                break;
            }


            int randomX = random.nextInt(mat.length);
            int randomY = random.nextInt(mat[randomX].length);
            switch (dd) {

                case 1: { //Senkrecht

                    boolean checker = false;


                    while (checker == false) {
                        try {
                            for (int t = 0; t < word[i].length(); t++) {

                                if (System.currentTimeMillis() - start > end) { // Abbruch nach X-Sekunden
                                    return fillMat(fileP, word, matrix, difC, end);
                                }
                                if (mat[randomX + t][randomY] == '\u0000' || mat[randomX + t][randomY] == "#".charAt(0)) { //Es wird getestet ob das wort reinpassst
                                    mat[randomX + t][randomY] = "#".charAt(0); //Platzhalter
                                } else {
                                    randomX = random.nextInt(mat.length); //Neue Startkoordinaten
                                    randomY = random.nextInt(mat[randomX].length);
                                }

                            }
                            for (int t = 0; t < word[i].length(); t++) {
                                if (System.currentTimeMillis() - start > end) {
                                    return fillMat(fileP, word, matrix, difC, end);
                                }
                                if (mat[randomX + t][randomY] == '\u0000' || mat[randomX + t][randomY] == "#".charAt(0)) { //Wort wird eingefügt
                                    mat[randomX + t][randomY] = word[i].charAt(t);;
                                } else {
                                    randomX = random.nextInt(mat.length);//Absicherung
                                    randomY = random.nextInt(mat[randomX].length);
                                }

                            }
                            checker = true;
                        } catch (Exception e) {
                            try {
                                for (int t = 0; t < word[i].length(); t++) {
                                    if (System.currentTimeMillis() - start > end) {
                                        return fillMat(fileP, word, matrix, difC, end);
                                    }
                                    if (mat[randomX + t][randomY] == '\u0000' || mat[randomX + t][randomY] == "#".charAt(0)) {
                                        mat[randomX + t][randomY] = "#".charAt(0);
                                    } else {
                                        randomX = random.nextInt(mat.length);
                                        randomY = random.nextInt(mat[randomX].length);
                                    }

                                }
                                for (int t = 0; t < word[i].length(); t++) {
                                    if (System.currentTimeMillis() - start > end) {
                                        return fillMat(fileP, word, matrix, difC, end);
                                    }
                                    if (mat[randomX + t][randomY] == '\u0000' || mat[randomX + t][randomY] == "#".charAt(0)) {
                                        mat[randomX + t][randomY] = word[i].charAt(t);;
                                    } else {
                                        randomX = random.nextInt(mat.length);
                                        randomY = random.nextInt(mat[randomX].length);
                                    }

                                }
                                checker = true;
                            } catch (Exception g) {
                                if (randomX != 0) {
                                    randomX--;
                                }
                            }
                        }
                    }
                }
                break;
                case 2: { //Waagerecht

                    boolean checker = false;

                    while (checker == false) {
                        try {
                            for (int t = 0; t < word[i].length(); t++) {
                                if (System.currentTimeMillis() - start > end) {
                                    return fillMat(fileP, word, matrix, difC, end);
                                }
                                if (mat[randomX][randomY + t] == '\u0000' || mat[randomX][randomY + t] == "#".charAt(0)) {
                                    mat[randomX][randomY + t] = "#".charAt(0);
                                } else {
                                    randomX = random.nextInt(mat.length);
                                    randomY = random.nextInt(mat[randomX].length);
                                }

                            }
                            for (int t = 0; t < word[i].length(); t++) {
                                if (System.currentTimeMillis() - start > end) {
                                    return fillMat(fileP, word, matrix, difC, end);
                                }
                                if (mat[randomX][randomY + t] == '\u0000' || mat[randomX][randomY + t] == "#".charAt(0)) {
                                    mat[randomX][randomY + t] = word[i].charAt(t);;
                                } else {
                                    randomX = random.nextInt(mat.length);
                                    randomY = random.nextInt(mat[randomX].length);
                                }

                            }
                            checker = true;
                        } catch (Exception e) {
                            try {
                                for (int t = 0; t < word[i].length(); t++) {
                                    if (System.currentTimeMillis() - start > end) {
                                        return fillMat(fileP, word, matrix, difC, end);
                                    }
                                    if (mat[randomX][randomY + t] == '\u0000' || mat[randomX][randomY + t] == "#".charAt(0)) {
                                        mat[randomX][randomY + t] = "#".charAt(0);
                                    } else {
                                        randomX = random.nextInt(mat.length);
                                        randomY = random.nextInt(mat[randomX].length);
                                    }

                                }
                                for (int t = 0; t < word[i].length(); t++) {
                                    if (System.currentTimeMillis() - start > end) {
                                        return fillMat(fileP, word, matrix, difC, end);
                                    }
                                    if (mat[randomX][randomY + t] == '\u0000' || mat[randomX][randomY + t] == "#".charAt(0)) {
                                        mat[randomX][randomY + t] = word[i].charAt(t);;
                                    } else {
                                        randomX = random.nextInt(mat.length);
                                        randomY = random.nextInt(mat[randomX].length);
                                    }

                                }
                                checker = true;
                            } catch (Exception g) {
                                if (randomX != 0) {
                                    randomX--;
                                }
                            }
                        }
                    }
                }
                break;
            }


        }


        char[] alphabet = null;
        switch (difC) { //Schwierigkeit beim Füllen der restlichen Felder
            case 5: {
                alphabet = toLetterArr(word).toCharArray();
            }
            break;
            default: {
                alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
            }
            break;
        }

        for (int h = 0; h < mat.length; h++) {
            for (int k = 0; k < mat[h].length; k++) {
                if (mat[h][k] == '\u0000' || mat[h][k] == "#".charAt(0)) {
                    int randAlpha = random.nextInt(alphabet.length);
                    mat[h][k] = alphabet[randAlpha];
                }
            }
        }

        return mat;
    }

    public static String[] wordArr(String fileLoc) throws IOException {//Wörterrray wird erstellt
        String size = readLine(fileLoc, 1);
        String[] sp = new String[Integer.valueOf(size)];
        int e = 2;
        for (int i = 0; i < Integer.valueOf(size); i++) {
            sp[i] = readLine(fileLoc, e);
            e++;
        }

        return sp;
    }

    public static void printArr(char[][] matrix) {//Array wird ausgegeben

        for (int i = 0; i < matrix.length; i++) { //reihe der matrix
            for (int j = 0; j < matrix[i].length; j++) { //zeile der reihe
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Zum schließen beliebige Taste drücken");
        System.console().readLine();
    }

    public static String toLetterArr(String[] words) {//WortArray zum Buchstabenstring
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            stringBuilder.append(words[i]);
        }
        String finalString = stringBuilder.toString();
        //System.out.println(finalString);
        return finalString;
    }
}
